package store.model;

import java.util.List;

public class ConvenienceStore {

    private final ProductInventory productInventory;

    public ConvenienceStore(List<Product> products) {
        productInventory = new ProductInventory(products);
    }

    public void order(OrderProduct orderProduct) {
        List<Product> products = productInventory.findByName(orderProduct.getName());
    }

}
