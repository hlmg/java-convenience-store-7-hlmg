package store.model;

import java.util.List;

public class ConvenienceStore {

    private final List<Product> products;
    private final List<Promotion> promotions;

    public ConvenienceStore(List<Product> products, List<Promotion> promotions) {
        this.products = products;
        this.promotions = promotions;
    }

    public boolean buy(OrderProduct orderProduct) {
        List<Product> findProducts = findProductByName(orderProduct.name());
        checkProductExist(findProducts);
        checkEnoughStock(findProducts, orderProduct.quantity());
        return true;
    }

    private void checkProductExist(List<Product> products) {
        if (products.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
    }

    private List<Product> findProductByName(String name) {
        return products.stream()
                .filter(product -> product.nameEquals(name))
                .toList();
    }

    private void checkEnoughStock(List<Product> products, int orderQuantity) {
        if (getStock(products) < orderQuantity) {
            throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다.");
        }
    }

    private int getStock(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

}
