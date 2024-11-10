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
            return proceedPromotionOrder(findProducts, orderProduct, activePromotion.get());
        }
        return proceedRegularOrder(findProducts, orderProduct);
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

    private BuyResult proceedPromotionOrder(List<Product> products, OrderProduct orderProduct, Promotion promotion) {
        if (isPromotionStockLow(products, orderProduct.quantity())) {
            return proceedLowStockPromotion(products, orderProduct, promotion);
        }
        return proceedFullStockPromotion(orderProduct, promotion);
    }

    private boolean isPromotionStockLow(List<Product> products, int buyQuantity) {
        int promotionStock = getPromotionStock(products);
        return promotionStock <= buyQuantity;
    }

    private BuyResult proceedLowStockPromotion(List<Product> products, OrderProduct orderProduct, Promotion promotion) {
        int promotionStock = getPromotionStock(products);
        PromotionResult promotionResult = promotion.apply(promotionStock);

        int pendingQuantity = (orderProduct.quantity() - promotionStock) + promotionResult.remain();
        return BuyResult.createPartiallyPromotedResult(promotionResult.buy(), promotionResult.get(),
                pendingQuantity);
    }

    private BuyResult proceedFullStockPromotion(OrderProduct orderProduct, Promotion promotion) {
        PromotionResult promotionResult = promotion.apply(orderProduct.quantity());

        int pendingQuantity = promotionResult.remain();
        if (promotion.isBonusApplicable(promotionResult.remain())) {
            return BuyResult.createBonusAddableResult(promotionResult.buy(), promotionResult.get(),
                    pendingQuantity);
        }
        return BuyResult.createPartiallyPromotedResult(promotionResult.buy(), promotionResult.get(),
                pendingQuantity);
    }

    private int getPromotionStock(List<Product> products) {
        return products.stream()
                .filter(Product::hasPromotion)
                .mapToInt(Product::getQuantity)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("프로모션 상품이 없습니다."));
    }

    private BuyResult proceedRegularOrder(List<Product> products, OrderProduct orderProduct) {
        // 일반 구매 진행
        return null;
    }

}
