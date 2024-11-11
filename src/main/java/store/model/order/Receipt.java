package store.model.order;

import java.util.Collections;
import java.util.List;
import store.model.discount.MembershipDiscount;
import store.model.user.UserInputCommand;

public class Receipt {

    private final MembershipDiscount membershipDiscount;
    private final List<BuyResult> buyResults;
    private final UserInputCommand membershipDecision;

    public Receipt(List<BuyResult> buyResults, UserInputCommand membershipDecision) {
        this.membershipDiscount = new MembershipDiscount();
        this.buyResults = buyResults;
        this.membershipDecision = membershipDecision;
    }

    public int getTotalBuyPrice() {
        return buyResults.stream()
                .mapToInt(BuyResult::getTotalBuyPrice)
                .sum();
    }

    public int getTotalBuyQuantity() {
        return buyResults.stream()
                .mapToInt(BuyResult::getTotalBuyQuantity)
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

    public List<BuyResult> getBuyResults() {
        return Collections.unmodifiableList(buyResults);
    }

}
