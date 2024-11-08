package store.controller;

import java.util.List;
import store.model.Product;
import store.model.Promotion;
import store.input.ProductFileReader;
import store.input.PromotionFileReader;

public class StoreController {

    private final PromotionFileReader promotionFileReader = new PromotionFileReader();
    private final ProductFileReader productFileReader = new ProductFileReader();

    public void run() {
        List<Promotion> promotions = promotionFileReader.getPromotions();
        List<Product> products = productFileReader.getProducts(promotions);
    }

}
