package store.model.order;

import store.model.product.SellingProductSnapshot;
import store.model.promotion.PromotionResult;
import store.model.user.UserInputCommand;

public final class BuyResult {

    private final SellingProductSnapshot sellingProduct;
    private final PromotionResult promotionResult;
    private int regularPriceQuantity;

    public BuyResult(SellingProductSnapshot sellingProduct, PromotionResult promotionResult, int regularPriceQuantity) {
        this.sellingProduct = sellingProduct;
        this.promotionResult = promotionResult;
        this.regularPriceQuantity = regularPriceQuantity;
    }

    public static BuyResult createPromotionResult(SellingProductSnapshot sellingProduct, PromotionResult promotionResult) {
        return new BuyResult(sellingProduct, promotionResult, 0);
    }

    public static BuyResult createRegularOrder(SellingProductSnapshot sellingProduct, int quantity) {
        return new BuyResult(sellingProduct, null, quantity);
    }

    public boolean isPendingState() {
        return isPromotionOrder() && promotionResult.isPending();
    }

    public boolean isPromotionOrder() {
        return promotionResult != null;
    }

    public void resolvePendingState(UserInputCommand userInputCommand) {
        if (!isPendingState()) {
            return;
        }
        if (promotionResult.isBonusAddable()) {
            applyBonusDecision(userInputCommand);
            return;
        }
        if (promotionResult.isPartiallyPromoted()) {
            applyRegularPricePaymentDecision(userInputCommand);
        }
    }

    public boolean hasBonus() {
        if (!isPromotionOrder()) {
            return false;
        }
        return promotionResult.hasBonus();
    }

    public int getTotalBuyPrice() {
        return sellingProduct.price() * getTotalBuyQuantity();
    }

    public int getPromotionDiscountPrice() {
        if (!isPromotionOrder()) {
            return 0;
        }
        return sellingProduct.price() * promotionResult.getBonusQuantity();
    }

    public int getRegularBuyPrice() {
        return sellingProduct.price() * regularPriceQuantity;
    }

    public int getTotalBuyQuantity() {
        if (!isPromotionOrder()) {
            return regularPriceQuantity;
        }
        return promotionResult.getPromotionPriceQuantity() + promotionResult.getBonusQuantity() + regularPriceQuantity;
    }

    public int getPendingQuantity() {
        return promotionResult.getPendingQuantity();
    }

    public boolean isBonusAddable() {
        return promotionResult.getPromotionState() == PromotionState.BONUS_ADDABLE;
    }

    public boolean isComplete() {
        return !isPromotionOrder() || promotionResult.getPromotionState() == PromotionState.FULL_PROMOTED;
    }

    public boolean isPartiallyPromoted() {
        return promotionResult.isPartiallyPromoted();
    }

    public String getProductName() {
        return sellingProduct.name();
    }

    public int getBonusQuantity() {
        return promotionResult.getBonusQuantity();
    }

    private void applyBonusDecision(UserInputCommand bonusDecision) {
        if (bonusDecision == UserInputCommand.YES) {
            promotionResult.addBonus();
        }
        if (bonusDecision == UserInputCommand.NO) {
            regularPriceQuantity = promotionResult.getPendingQuantity();
        }
        promotionResult.changeToFullPromotedState();
    }

    private void applyRegularPricePaymentDecision(UserInputCommand regularPricePaymentDecision) {
        if (regularPricePaymentDecision == UserInputCommand.YES) {
            regularPriceQuantity = promotionResult.getPendingQuantity();;
        }
        promotionResult.changeToFullPromotedState();
    }

}
