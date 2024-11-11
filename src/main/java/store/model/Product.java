package store.model;

public record Product(String name, int price, int quantity, String promotion) {

    public boolean nameEquals(String name) {
        return this.name.equals(name);
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public int deductStock(int totalBuyQuantity) {
        throw new UnsupportedOperationException();
    }

}
