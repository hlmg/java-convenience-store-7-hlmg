package store.model;

public class Product {

    private final String name;
    private final int price;
    private final int quantity;
    private final String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public boolean nameEquals(String name) {
        return this.name.equals(name);
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

}
