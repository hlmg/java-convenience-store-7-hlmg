package store.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductInventory {

    private final Map<String, List<Product>> nameToProducts = new HashMap<>();

    public void saveAll(List<Product> products) {
        for (Product product : products) {
            save(product);
        }
    }

    private void save(Product product) {
        List<Product> products = nameToProducts.getOrDefault(product.getName(), new ArrayList<>());
        products.add(product);
        nameToProducts.put(product.getName(), products);
    }

}
