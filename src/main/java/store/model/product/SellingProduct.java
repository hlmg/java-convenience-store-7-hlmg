package store.model.product;

import store.model.order.BuyType;

public class SellingProduct {

    private final String name;
    private final int price;
    private int regularStock;
    private int promotionStock;
    private String promotion;

    public SellingProduct(String name, int price, int regularStock, int promotionStock, String promotion) {
        this.name = name;
        this.price = price;
        this.regularStock = regularStock;
        this.promotionStock = promotionStock;
        this.promotion = promotion;
    }

    public static SellingProduct from(Product product) {
        if (product.hasPromotion()) {
            return new SellingProduct(product.getName(), product.getPrice(), 0, product.getQuantity(),
                    product.getPromotion());
        }
        return new SellingProduct(product.getName(), product.getPrice(), product.getQuantity(), 0,
                product.getPromotion());
    }

    public void update(Product product) {
        if (product.hasPromotion()) {
            this.promotionStock = product.getQuantity();
            this.promotion = product.getPromotion();
            return;
        }
        this.regularStock = product.getQuantity();
    }

    public boolean ifPromotionStockLessThanOrEquals(int quantity) {
        return promotionStock <= quantity;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return regularStock + promotionStock;
    }

    public String getPromotion() {
        return promotion;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public int getRegularStock() {
        return regularStock;
    }

    public int getPrice() {
        return price;
    }

    public void deductStock(int totalBuyQuantity, BuyType buyType) {
        if (buyType == BuyType.PROMOTION) {
            deductPromotionStockFirst(totalBuyQuantity);
            return;
        }
        deductRegularStockFirst(totalBuyQuantity);
    }

    private void deductRegularStockFirst(int totalBuyQuantity) {
        if (regularStock >= totalBuyQuantity) {
            regularStock -= totalBuyQuantity;
            return;
        }
        int remain = totalBuyQuantity - regularStock;
        regularStock = 0;
        promotionStock -= remain;
    }

    private void deductPromotionStockFirst(int totalBuyQuantity) {
        if (promotionStock >= totalBuyQuantity) {
            promotionStock -= totalBuyQuantity;
            return;
        }
        int remain = totalBuyQuantity - promotionStock;
        promotionStock = 0;
        regularStock -= remain;
    }

    public boolean hasPromotion() {
        return this.promotion != null;
    }

}