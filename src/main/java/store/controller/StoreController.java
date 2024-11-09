package store.controller;

import java.util.List;
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
        for (OrderProduct orderProduct : orderProducts) {
            purchase(orderProduct);
        }
    }

    private void init() {
        List<Product> products = productFileReader.getProducts();
        List<Promotion> promotions = promotionFileReader.getPromotions();
        convenienceStore = new ConvenienceStore(products, promotions);
    }

    private void purchase(OrderProduct orderProduct) {
        convenienceStore.buy(orderProduct);
    }

}
