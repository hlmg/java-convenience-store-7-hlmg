package store.io;

import java.util.List;
import store.exception.StoreException;
import store.model.order.BuyResult;
import store.model.order.Receipt;
import store.model.product.SellingProductSnapshot;

public class OutputView {

    public void printGreetingComment() {
        println("안녕하세요. W편의점입니다.");
    }

    public void printSellingProducts(List<SellingProductSnapshot> productSnapshots) {
        println("현재 보유하고 있는 상품입니다.");
        printEmptyLine();
        productSnapshots.forEach(this::printProduct);
        printEmptyLine();
    }

    public void printReceipt(Receipt receipt) {
        println("==============W 편의점================");
        printBuyProducts(receipt.getBuyResults());
        printBonus(receipt.getBuyResults());
        printPaymentInfo(receipt);
        printEmptyLine();
    }

    public void printStoreException(StoreException e) {
        String message = String.format("[ERROR] %s 다시 입력해 주세요.", e.getMessage());
        println(message);
    }

    private void printBuyProducts(List<BuyResult> buyResults) {
        println(String.format("%-19s%-10s%-6s", "상품명", "수량", "금액"));
        for (BuyResult buyResult : buyResults) {
            String productName = buyResult.getProductName();
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
                String productName = buyResult.getProductName();
                int bonusQuantity = buyResult.getBonusQuantity();
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
        return quantity + "개";
    }

    private void printProduct(SellingProductSnapshot productSnapshots) {
        if (productSnapshots.hasPromotion()) {
            printPromotionProduct(productSnapshots);
        }
        printRegularProduct(productSnapshots);
    }

    private void printPromotionProduct(SellingProductSnapshot product) {
        String stockMessage = getStockMessage(product.promotionStock());
        String productMessage = String.format("- %s %,d원 %s %s", product.name(), product.price(), stockMessage,
                product.promotion());
        println(productMessage);
    }

    private void printRegularProduct(SellingProductSnapshot product) {
        String stockMessage = getStockMessage(product.regularStock());
        String productMessage = String.format("- %s %,d원 %s", product.name(), product.price(), stockMessage);
        println(productMessage);
    }

    private void println(String message) {
        System.out.println(message);
    }

    private void printEmptyLine() {
        System.out.println();
    }

}
