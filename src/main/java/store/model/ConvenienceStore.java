package store.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ConvenienceStore {

    private final List<Product> products;
    private final List<Promotion> promotions;

    public ConvenienceStore(List<Product> products, List<Promotion> promotions) {
        this.products = products;
        this.promotions = promotions;
    }

    public BuyResult buy(OrderProduct orderProduct, LocalDate orderDate) {
        List<Product> findProducts = findProductsByName(orderProduct.name());
        checkProductExist(findProducts);
        checkEnoughStock(findProducts, orderProduct.quantity());
        Optional<Promotion> activePromotion = getActivePromotion(findProducts, orderDate);
        if (activePromotion.isPresent()) {
            return processPromotionOrder(orderProduct, activePromotion.get());
        }
        return processRegularOrder(orderProduct);
    }

    private Optional<Promotion> findPromotionByName(String promotionName) {
        return promotions.stream()
                .filter(promotion -> promotion.nameEquals(promotionName))
                .findAny();
    }

    private void checkProductExist(List<Product> products) {
        if (products.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
    }

    private List<Product> findProductsByName(String name) {
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

    private Optional<Promotion> getActivePromotion(List<Product> products, LocalDate orderDate) {
        return products.stream()
                .map(Product::getPromotion)
                .map(this::findPromotionByName)
                .flatMap(Optional::stream)
                .filter(promotion -> promotion.isActiveOn(orderDate))
                .findAny();
    }

    private BuyResult processPromotionOrder(OrderProduct orderProduct, Promotion promotion) {
        // 프로모션 구매 진행
        return null;
    }

    private BuyResult processRegularOrder(OrderProduct orderProduct) {
        // 일반 구매 진행
        return null;
    }

}
