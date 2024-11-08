package store.model;

public class OrderProduct {

    private final String name;
    private final int quantity;

    public OrderProduct(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

}
