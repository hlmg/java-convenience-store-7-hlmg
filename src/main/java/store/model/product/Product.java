package store.model.product;

public final class Product {

    private final String name;
    private final int price;
    private int quantity;
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

    public boolean hasPromotion() {
        return promotion != null;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }

    public int deductStock(int quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
            return 0;
        }
        int remain = quantity - this.quantity;
        this.quantity = 0;
        return remain;
    }

}
