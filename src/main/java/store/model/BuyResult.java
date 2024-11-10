package store.model;

public class BuyResult {

    private final BuyType buyType;
    private final BuyState buyState;
    private final int promotionPriceQuantity;
    private final int bonusQuantity;
    private final int pendingQuantity;
    private final int regularPriceQuantity;

    public BuyResult(BuyType buyType, BuyState buyState, int promotionPriceQuantity, int bonusQuantity,
                     int pendingQuantity,
                     int regularPriceQuantity) {
        this.buyType = buyType;
        this.buyState = buyState;
        this.promotionPriceQuantity = promotionPriceQuantity;
        this.bonusQuantity = bonusQuantity;
        this.pendingQuantity = pendingQuantity;
        this.regularPriceQuantity = regularPriceQuantity;
    }

    public static BuyResult createPromotionCompleteResult(int promotionPriceQuantity, int bonusQuantity, int regularPriceQuantity) {
        return new BuyResult(BuyType.PROMOTION, BuyState.COMPLETE,
                promotionPriceQuantity, bonusQuantity, 0, regularPriceQuantity);
    }

    public static BuyResult createPartiallyPromotedResult(int promotionPriceQuantity, int bonusQuantity,
                                                          int pendingQuantity) {
        if (pendingQuantity == 0) {
            return createPromotionCompleteResult(promotionPriceQuantity, bonusQuantity, 0);
        }
        return new BuyResult(BuyType.PROMOTION, BuyState.PARTIALLY_PROMOTED,
                promotionPriceQuantity, bonusQuantity, pendingQuantity, 0);
    }

    public static BuyResult createBonusAddableResult(int promotionPriceQuantity, int bonusQuantity,
                                                     int pendingQuantity) {
        return new BuyResult(BuyType.PROMOTION, BuyState.BONUS_ADDABLE,
                promotionPriceQuantity, bonusQuantity, pendingQuantity, 0);
    }

    public static BuyResult createRegularCompleteResult(int regularPriceQuantity) {
        return new BuyResult(BuyType.REGULAR, BuyState.COMPLETE,
                0, 0, 0, regularPriceQuantity);
    }

    public BuyType getBuyType() {
        return buyType;
    }

    public BuyState getBuyState() {
        return buyState;
    }

    public int getPromotionPriceQuantity() {
        return promotionPriceQuantity;
    }

    public int getBonusQuantity() {
        return bonusQuantity;
    }

    public int getPendingQuantity() {
        return pendingQuantity;
    }

    public int getRegularPriceQuantity() {
        return regularPriceQuantity;
    }

}
