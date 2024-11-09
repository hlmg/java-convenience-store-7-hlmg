package store.model;

import java.util.List;

public class ConvenienceStore {

    private final ProductInventory productInventory = new ProductInventory();

    public ConvenienceStore(List<Product> products) {
        productInventory.saveAll(products);
    }

}
