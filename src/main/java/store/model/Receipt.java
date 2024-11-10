package store.model;

import java.util.List;

public class Receipt {

    private final MembershipDiscount membershipDiscount;
    private final List<BuyResult> buyResults;
    private UserInputCommand membershipDecision;

    public Receipt(List<BuyResult> buyResults) {
        this.membershipDiscount = new MembershipDiscount();
        this.buyResults = buyResults;
        this.membershipDecision = UserInputCommand.NO;
    }

    public void updateMembershipDecision(UserInputCommand membershipDecision) {
        this.membershipDecision = membershipDecision;
    }

    public int getTotalBuyPrice() {
        return buyResults.stream()
                .mapToInt(BuyResult::getTotalBuyPrice)
                .sum();
    }

    public int getPromotionDiscountPrice() {
        return buyResults.stream()
                .mapToInt(BuyResult::getPromotionDiscountPrice)
                .sum();
    }

    public int getMembershipDiscountPrice() {
        if (membershipDecision == UserInputCommand.NO) {
            return 0;
        }
        int regularBuyPrice = buyResults.stream()
                .mapToInt(BuyResult::getRegularBuyPrice)
                .sum();
        return membershipDiscount.getDiscountAmount(regularBuyPrice);
    }

    public int getPaymentPrice() {
        return getTotalBuyPrice() - getPromotionDiscountPrice() - getMembershipDiscountPrice();
    }

}
