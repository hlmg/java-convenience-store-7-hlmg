package store.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.exception.StoreException;

public class SellingProducts {

    private final Map<String, SellingProduct> sellingProducts = new LinkedHashMap<>();

    public SellingProducts(List<Product> products) {
        for (Product product : products) {
            String name = product.getName();
            if (sellingProducts.containsKey(name)) {
                sellingProducts.get(name).update(product);
                continue;
            }
            sellingProducts.put(name, SellingProduct.from(product));
        }
    }

    public SellingProduct getProductBy(String name) {
        SellingProduct sellingProduct = sellingProducts.get(name);
        if (sellingProduct == null) {
            throw new StoreException("존재하지 않는 상품입니다.");
        }
        return sellingProduct;
    }

    public List<SellingProduct> getProducts() {
        Collection<SellingProduct> values = sellingProducts.values();

        return sellingProducts.values().stream().toList();
    }

}
