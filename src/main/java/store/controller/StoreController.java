package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;
import store.exception.StoreException;
import store.io.reader.ProductFileReader;
import store.io.reader.PromotionFileReader;
import store.io.InputView;
import store.io.OutputView;
import store.model.ConvenienceStore;
import store.model.order.BuyResult;
import store.model.order.OrderProduct;
import store.model.order.Receipt;
import store.model.product.Product;
import store.model.promotion.Promotion;
import store.model.user.UserInputCommand;

public class StoreController {

    private final ProductFileReader productFileReader = new ProductFileReader();
    private final PromotionFileReader promotionFileReader = new PromotionFileReader();
    private final OutputView outputView = new OutputView();
    private final InputView inputView = new InputView();
    private OrderState orderState;
    private ConvenienceStore convenienceStore;

    public void run() {
        storeInitialize();
        while (orderState.inProgress()) {
            orderState = proceedOrder();
        }
    }

    private void storeInitialize() {
        List<Product> products = productFileReader.getProducts();
        List<Promotion> promotions = promotionFileReader.getPromotions();
        convenienceStore = new ConvenienceStore(products, promotions);
        orderState = OrderState.IN_PROGRESS;
    }

    private OrderState proceedOrder() {
        showStoreInformation();
        List<BuyResult> buyResults = handleRetryableException(this::buyProducts);
        processPendingResults(buyResults);
        Receipt receipt = getReceipt(buyResults);
        outputView.printReceipt(receipt);
        convenienceStore.deductProductsStock(receipt.getBuyResults());
        return orderState.additionalOrder(handleRetryableException(inputView::askAdditionalPurchase));
    }

    private void showStoreInformation() {
        outputView.printGreetingComment();
        outputView.printSellingProducts(convenienceStore.getSellingProducts());
    }

    private List<BuyResult> buyProducts() {
        List<OrderProduct> orderProducts = inputView.getOrderProductsFromUser();
        LocalDate orderDate = DateTimes.now().toLocalDate();
        return convenienceStore.buyProducts(orderProducts, orderDate);
    }

    private void processPendingResults(List<BuyResult> buyResults) {
        for (BuyResult buyResult : buyResults) {
            if (buyResult.isComplete()) {
                continue;
            }
            UserInputCommand userInputCommand = handleRetryableException(
                    () -> inputView.getUserInputCommand(buyResult));
            buyResult.resolvePendingState(userInputCommand);
        }
    }

    private Receipt getReceipt(List<BuyResult> buyResults) {
        UserInputCommand membershipDecision = handleRetryableException(inputView::askMembershipDiscount);
        return new Receipt(buyResults, membershipDecision);
    }

    private <T> T handleRetryableException(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (StoreException e) {
                outputView.printStoreException(e);
            }
        }
    }

}
