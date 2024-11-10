package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.List;
import store.model.BuyResult;
import store.model.BuyState;
import store.model.ConvenienceStore;
import store.model.OrderProduct;
import store.model.Product;
import store.model.Promotion;
import store.input.ProductFileReader;
import store.input.PromotionFileReader;

public class StoreController {

    private final ProductFileReader productFileReader = new ProductFileReader();
    private final PromotionFileReader promotionFileReader = new PromotionFileReader();
    private ConvenienceStore convenienceStore;

    public void run() {
        init();
        List<OrderProduct> orderProducts = List.of(
                new OrderProduct("콜라", 3),
                new OrderProduct("에너지바", 5)
        );
        LocalDate orderDate = DateTimes.now().toLocalDate();
        for (OrderProduct orderProduct : orderProducts) {
            purchase(orderProduct, orderDate);
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
            return null; // 보너스 추가 여부 묻기
        }
        if (buyResult.buyState() == BuyState.PARTIALLY_PROMOTED) {
            return null; // 정가 결제 여부 묻기
        }
        throw new IllegalStateException("지원하지 않는 주문 상태입니다.");
    }

}
