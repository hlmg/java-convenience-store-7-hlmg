package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;
import store.exception.StoreException;
import store.input.ProductFileReader;
import store.input.PromotionFileReader;
import store.io.InputView;
import store.io.OutputView;
import store.model.ConvenienceStore;
import store.model.order.BuyResult;
import store.model.order.BuyState;
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
    private ConvenienceStore convenienceStore;

    public void run() {
        init();

        while (true) {
            outputView.printGreetingComment();
            outputView.printSellingProducts(convenienceStore.getSellingProducts());
            List<BuyResult> buyResults = handleRetryableException(this::order);

            resolvePendingResult(buyResults);

            Receipt receipt = new Receipt(buyResults);

            UserInputCommand membershipDecision = handleRetryableException(inputView::askMembershipDiscount);
            receipt.updateMembershipDecision(membershipDecision);
            outputView.printReceipt(receipt);
            convenienceStore.deductProductsStock(receipt.getBuyResults());

            UserInputCommand additionalPurchase = handleRetryableException(inputView::askAdditionalPurchase);
            if (additionalPurchase == UserInputCommand.NO) {
                break;
            }
        }
    }

    private void init() {
        List<Product> products = productFileReader.getProducts();
        List<Promotion> promotions = promotionFileReader.getPromotions();
        convenienceStore = new ConvenienceStore(products, promotions);
    }

    private List<BuyResult> order() {
        List<OrderProduct> orderProducts = inputView.getOrderProductsFromUser();
        LocalDate orderDate = DateTimes.now().toLocalDate();
        return convenienceStore.order(orderProducts, orderDate);
    }

    private void resolvePendingResult(List<BuyResult> buyResults) {
        for (BuyResult buyResult : buyResults) {
            if (buyResult.buyState() == BuyState.COMPLETE) {
                continue;
            }
            UserInputCommand userInputCommand = handleRetryableException(() -> inputView.getUserInputCommand(buyResult));
            buyResult.resolvePendingState(userInputCommand);
        }
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
