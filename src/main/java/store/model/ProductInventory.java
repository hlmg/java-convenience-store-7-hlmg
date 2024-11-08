package store.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductInventory {

    private static final String PRODUCT_NOT_FOUND = "존재하지 않는 상품입니다.";
    private final Map<String, List<Product>> nameToProducts = new HashMap<>();

    public ProductInventory(List<Product> products) {
        for (Product product : products) {
            save(product);
        }
    }

    private void save(Product product) {
        List<Product> products = nameToProducts.getOrDefault(product.getName(), new ArrayList<>());
        products.add(product);
        nameToProducts.put(product.getName(), products);
    }

    public List<Product> findByName(String name) {
        List<Product> products = nameToProducts.get(name);
        if (products == null) {
            throw new IllegalArgumentException(PRODUCT_NOT_FOUND);
        }
        return Collections.unmodifiableList(products);
    }

}
