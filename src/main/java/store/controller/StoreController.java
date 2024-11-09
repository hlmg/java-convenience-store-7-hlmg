package store.controller;

import java.util.List;
import store.input.ProductFileReader;
import store.input.PromotionFileReader;
import store.model.ConvenienceStore;
import store.model.Product;
import store.model.Promotion;

public class StoreController {

    private final PromotionFileReader promotionFileReader = new PromotionFileReader();
    private final ProductFileReader productFileReader = new ProductFileReader();

    public void run() {
        List<Promotion> promotions = promotionFileReader.getPromotions();
        List<Product> products = productFileReader.getProducts(promotions);

        ConvenienceStore convenienceStore = new ConvenienceStore(products);
    }

}
