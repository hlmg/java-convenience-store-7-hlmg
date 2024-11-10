package store.io;

import java.util.List;
import store.model.Product;

public class OutputView {

    public void printGreetingComment() {
        println("안녕하세요. W편의점입니다.");
    }

    public void printProducts(List<Product> products) {
        println("현재 보유하고 있는 상품입니다.");
        printEmptyLine();
        products.forEach(this::printProduct);
        printEmptyLine();
    }

    private void printProduct(Product product) {
        String stockMessage = getStockMessage(product.quantity());
        String productMessage = String.format("- %s %,d원 %s개", product.name(), product.price(), stockMessage);
        if (product.hasPromotion()) {
            productMessage += " " + product.promotion();
        }
        println(productMessage);
    }

    private String getStockMessage(int quantity) {
        if (quantity == 0) {
            return "재고 없음";
        }
        return String.valueOf(quantity);
    }

    private void println(String message) {
        System.out.println(message);
    }

    private void printEmptyLine() {
        System.out.println();
    }

}
