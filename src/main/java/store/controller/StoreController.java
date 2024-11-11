package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.io.InputView;
import store.io.OutputView;
import store.model.BuyResult;
import store.model.BuyState;
import store.model.ConvenienceStore;
import store.model.OrderProduct;
import store.model.Product;
import store.model.Promotion;
import store.input.ProductFileReader;
import store.input.PromotionFileReader;
import store.model.Receipt;
import store.model.UserInputCommand;

public class StoreController {

    private final ProductFileReader productFileReader = new ProductFileReader();
    private final PromotionFileReader promotionFileReader = new PromotionFileReader();
    private final OutputView outputView = new OutputView();
    private final InputView inputView = new InputView();
    private ConvenienceStore convenienceStore;

    public void run() {
        init();

        while(true) {
            outputView.printGreetingComment();
            outputView.printProducts(convenienceStore.getProducts());
            List<OrderProduct> orderProducts = inputView.getOrderProductsFromUser();
            LocalDate orderDate = DateTimes.now().toLocalDate();
            // TODO: 상품을 구매하기 전에, 존재하지 않는 상품인지, 재고 수량을 초과했는지, 중복 상품이 존재하는지 확인해야 함.
            List<BuyResult> buyResults = new ArrayList<>();
            for (OrderProduct orderProduct : orderProducts) {
                buyResults.add(purchase(orderProduct, orderDate));
            }
            Receipt receipt = new Receipt(buyResults);
            UserInputCommand membershipDecision = inputView.askMembershipDiscount();
            receipt.updateMembershipDecision(membershipDecision);
            outputView.printReceipt(receipt);
            convenienceStore.deductProductsStock(receipt.getBuyResults());
            UserInputCommand additionalPurchase = inputView.askAdditionalPurchase();
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

    private BuyResult purchase(OrderProduct orderProduct, LocalDate orderDate) {
        BuyResult buyResult = convenienceStore.buy(orderProduct, orderDate);
        if (buyResult.buyState() == BuyState.COMPLETE) {
            return buyResult;
        }
        if (buyResult.buyState() == BuyState.BONUS_ADDABLE) {
            UserInputCommand bonusAddDecision = inputView.askAddBonus(buyResult.productName());
            return buyResult.applyBonusDecision(bonusAddDecision);
        }
        if (buyResult.buyState() == BuyState.PARTIALLY_PROMOTED) {
            UserInputCommand regularPricePaymentDecision = inputView.askRegularPricePayment(buyResult.productName(),
                    buyResult.pendingQuantity());
            return buyResult.applyRegularPricePaymentDecision(regularPricePaymentDecision);
        }
        throw new IllegalStateException("지원하지 않는 주문 상태입니다.");
    }

}
