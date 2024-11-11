package store.model;

import java.time.LocalDate;
import java.util.Collections;
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
        return proceedRegularOrder(orderProduct);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void deductProductsStock(List<BuyResult> buyResults) {
        // TODO: 로직 개선하기
        for (BuyResult buyResult : buyResults) {
            String productName = buyResult.productName();
            List<Product> products = findProductsByName(productName);
            Optional<Product> promotionProduct = findPromotionProduct(products);
            Optional<Product> regularProduct = findRegularProduct(products);
            if (buyResult.buyType() == BuyType.PROMOTION) {
                int buyQuantity = buyResult.getTotalBuyQuantity();
                if (promotionProduct.isPresent()) {
                    Product product = promotionProduct.get();
                    buyQuantity = product.deductStock(buyQuantity);
                }
                if (regularProduct.isPresent()) {
                    Product product = regularProduct.get();
                    product.deductStock(buyQuantity);
                }
                continue;
            }
            int buyQuantity = buyResult.getTotalBuyQuantity();
            if (regularProduct.isPresent()) {
                Product product = regularProduct.get();
                buyQuantity = product.deductStock(buyQuantity);
            }
            if (promotionProduct.isPresent()) {
                Product product = promotionProduct.get();
                product.deductStock(buyQuantity);
            }
        }
    }

    private Optional<Product> findPromotionProduct(List<Product> products) {
        return products.stream()
                .filter(Product::hasPromotion)
                .findAny();
    }

    private Optional<Product> findRegularProduct(List<Product> products) {
        return products.stream()
                .filter(product -> !product.hasPromotion())
                .findAny();
    }

    private List<Product> findProductsByName(String name) {
        return products.stream()
                .filter(product -> product.nameEquals(name))
                .toList();
    }

    private void checkProductExist(List<Product> products) {
        if (products.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
    }

    private void checkEnoughStock(List<Product> products, int orderQuantity) {
        if (getStock(products) < orderQuantity) {
            throw new IllegalArgumentException("재고 수량을 초과하여 구매할 수 없습니다.");
        }
    }

    private int getStock(List<Product> products) {
        return products.stream()
                .mapToInt(Product::quantity)
                .sum();
    }

    private Optional<Promotion> getActivePromotion(List<Product> products, LocalDate orderDate) {
        return products.stream()
                .map(Product::promotion)
                .map(this::findPromotionByName)
                .flatMap(Optional::stream)
                .filter(promotion -> promotion.isActiveOn(orderDate))
                .findAny();
    }

    private Optional<Promotion> findPromotionByName(String promotionName) {
        return promotions.stream()
                .filter(promotion -> promotion.nameEquals(promotionName))
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
        int price = findPriceByName(orderProduct.name());
        return BuyResult.createPartiallyPromotedResult(orderProduct.name(), price, promotionResult.buy(),
                promotionResult.get(),
                pendingQuantity);
    }

    private BuyResult proceedFullStockPromotion(OrderProduct orderProduct, Promotion promotion) {
        PromotionResult promotionResult = promotion.apply(orderProduct.quantity());
        int pendingQuantity = promotionResult.remain();
        int price = findPriceByName(orderProduct.name());
        if (promotion.isBonusApplicable(promotionResult.remain())) {
            return BuyResult.createBonusAddableResult(orderProduct.name(), price, promotionResult.buy(),
                    promotionResult.get(), pendingQuantity);
        }
        return BuyResult.createPartiallyPromotedResult(orderProduct.name(), price, promotionResult.buy(),
                promotionResult.get(), pendingQuantity);
    }

    private int getPromotionStock(List<Product> products) {
        return products.stream()
                .filter(Product::hasPromotion)
                .mapToInt(Product::quantity)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("프로모션 상품이 없습니다."));
    }

    private int findPriceByName(String name) {
        Product findProduct = products.stream()
                .filter(product -> product.nameEquals(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        return findProduct.price();
    }

    private BuyResult proceedRegularOrder(OrderProduct orderProduct) {
        int price = findPriceByName(orderProduct.name());
        return BuyResult.createRegularCompleteResult(orderProduct.name(), price, orderProduct.quantity());
    }

}
