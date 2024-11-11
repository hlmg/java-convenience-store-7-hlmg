package store.model.promotion;

import store.model.order.PromotionState;

public final class PromotionResult {

    private int promotionPriceQuantity;
    private int bonusQuantity;
    private int pendingQuantity;
    private PromotionState promotionState;

    public PromotionResult(int promotionPriceQuantity, int bonusQuantity, int pendingQuantity, PromotionState promotionState) {
        this.promotionPriceQuantity = promotionPriceQuantity;
        this.bonusQuantity = bonusQuantity;
        this.pendingQuantity = pendingQuantity;
        this.promotionState = promotionState;
    }

    public int getPromotionPriceQuantity() {
        return promotionPriceQuantity;
    }

    public int getBonusQuantity() {
        return bonusQuantity;
    }


    public void addBonus() {
        promotionPriceQuantity += pendingQuantity;
        bonusQuantity++;
    }

    public boolean isPending() {
        return isBonusAddable() || isPartiallyPromoted();
    }

    public boolean isBonusAddable() {
        return promotionState == PromotionState.BONUS_ADDABLE;
    }


    public boolean isPartiallyPromoted() {
        return promotionState == PromotionState.PARTIALLY_PROMOTED;
    }

    public void changeToFullPromotedState() {
        this.promotionState = PromotionState.FULL_PROMOTED;
        pendingQuantity = 0;
    }

    public PromotionState getPromotionState() {
        return promotionState;
    }

    public int getPendingQuantity() {
        return pendingQuantity;
    }

    public boolean hasBonus() {
        return bonusQuantity > 0;
    }

}
