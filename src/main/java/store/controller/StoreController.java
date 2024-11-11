package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import store.exception.StoreException;
import store.input.ProductFileReader;
import store.input.PromotionFileReader;
import store.io.InputView;
import store.io.OutputView;
import store.model.order.BuyResult;
import store.model.order.BuyState;
import store.model.ConvenienceStore;
import store.model.order.OrderProduct;
import store.model.product.Product;
import store.model.promotion.Promotion;
import store.model.order.Receipt;
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
            handleBuyResults(buyResults);
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
        checkDuplicate(orderProducts);
        LocalDate orderDate = DateTimes.now().toLocalDate();
        return convenienceStore.order(orderProducts, orderDate);
    }

    // TODO: orderProducts 만들어서 검증 로직 넘기기
    private void checkDuplicate(List<OrderProduct> orderProducts) {
        if (isDuplicate(orderProducts)) {
            throw new StoreException("잘못된 입력입니다.");
        }
    }

    private boolean isDuplicate(List<OrderProduct> orderProducts) {
        Set<String> distinctProductNames = new HashSet<>();
        return !orderProducts.stream()
                .map(OrderProduct::name)
                .allMatch(distinctProductNames::add);
    }

    private void handleBuyResults(List<BuyResult> buyResults) {
        for (BuyResult buyResult : buyResults) {
            if (buyResult.buyState() == BuyState.BONUS_ADDABLE) {
                UserInputCommand bonusAddDecision = handleRetryableException(
                        () -> inputView.askAddBonus(buyResult.productName()));
                buyResult.applyBonusDecision(bonusAddDecision);
                continue;
            }
            if (buyResult.buyState() == BuyState.PARTIALLY_PROMOTED) {
                UserInputCommand regularPricePaymentDecision = handleRetryableException(
                        () -> inputView.askRegularPricePayment(buyResult.productName(), buyResult.pendingQuantity()));
                buyResult.applyRegularPricePaymentDecision(regularPricePaymentDecision);
            }
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
