package store.io;

import java.util.List;
import store.model.BuyResult;
import store.model.Product;
import store.model.Receipt;

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

    public void printReceipt(Receipt receipt) {
        println("==============W 편의점================");
        printBuyProducts(receipt.getBuyResults());
        printBonus(receipt.getBuyResults());
        printPaymentInfo(receipt);
        printEmptyLine();
    }

    private void printBuyProducts(List<BuyResult> buyResults) {
        println(String.format("%-19s%-10s%-6s", "상품명", "수량", "금액"));
        for (BuyResult buyResult : buyResults) {
            String productName = buyResult.productName();
            int buyQuantity = buyResult.getTotalBuyQuantity();
            String buyPrice = String.format("%,d", buyResult.getTotalBuyPrice());
            String message = String.format("%-19s%-10s%-6s", productName, buyQuantity, buyPrice);
            println(message);
        }
    }

    private void printBonus(List<BuyResult> buyResults) {
        println("=============증     정===============");
        for (BuyResult buyResult : buyResults) {
            if (buyResult.hasBonus()) {
                String productName = buyResult.productName();
                int bonusQuantity = buyResult.bonusQuantity();
                String message = String.format("%-19s%-16s", productName, bonusQuantity);
                println(message);
            }
        }
    }

    private void printPaymentInfo(Receipt receipt) {
        println("====================================");
        printTotalBuyPriceAndQuantity(receipt.getTotalBuyQuantity(), receipt.getTotalBuyPrice());
        printPromotionDiscountPrice(receipt.getPromotionDiscountPrice());
        printMembershipDiscountPrice(receipt.getMembershipDiscountPrice());
        printPaymentPrice(receipt.getPaymentPrice());
    }

    private void printTotalBuyPriceAndQuantity(int totalBuyQuantity, int totalBuyPrice) {
        String price = String.format("%,d", totalBuyPrice);
        println(String.format("%-19s%-10s%-6s", "총구매액", totalBuyQuantity, price));
    }

    private void printPromotionDiscountPrice(int promotionDiscountPrice) {
        String price = String.format("-%,d", promotionDiscountPrice);
        println(String.format("%-29s%-6s", "행사할인", price));
    }

    private void printMembershipDiscountPrice(int membershipDiscountPrice) {
        String price = String.format("-%,d", membershipDiscountPrice);
        println(String.format("%-29s%-6s", "멤버십할인", price));
    }

    private void printPaymentPrice(int paymentPrice) {
        String price = String.format("%,d", paymentPrice);
        println(String.format("%-29s%-6s", "내실돈", price));
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
