package store.controller;

import java.util.List;
import store.model.Product;
import store.input.ProductFileReader;

public class StoreController {

    private final ProductFileReader productFileReader = new ProductFileReader();

    public void run() {
        List<Product> products = productFileReader.getProducts();
    }

}
