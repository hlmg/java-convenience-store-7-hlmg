package store.model.product;

public record SellingProductSnapshot(String name, int price, int regularStock, int promotionStock, String promotion) {

    public boolean hasPromotion() {
        return this.promotion != null;
    }

}
