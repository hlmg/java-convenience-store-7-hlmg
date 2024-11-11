package store.model.order;

import store.model.user.UserInputCommand;

public final class BuyResult {

    private final String productName;
    private final int price;
    private final BuyType buyType;
    private BuyState buyState;
    private int promotionPriceQuantity;
    private int bonusQuantity;
    private int pendingQuantity;
    private int regularPriceQuantity;

    public BuyResult(String productName, int price, BuyType buyType, BuyState buyState, int promotionPriceQuantity,
                     int bonusQuantity, int pendingQuantity, int regularPriceQuantity) {
        this.productName = productName;
        this.price = price;
        this.buyType = buyType;
        this.buyState = buyState;
        this.promotionPriceQuantity = promotionPriceQuantity;
        this.bonusQuantity = bonusQuantity;
        this.pendingQuantity = pendingQuantity;
        this.regularPriceQuantity = regularPriceQuantity;
    }

    public static BuyResult createPromotionCompleteResult(String productName, int price, int promotionPriceQuantity,
                                                          int bonusQuantity, int regularPriceQuantity) {
        return new BuyResult(productName, price, BuyType.PROMOTION, BuyState.COMPLETE,
                promotionPriceQuantity, bonusQuantity, 0, regularPriceQuantity);
    }

    public static BuyResult createPartiallyPromotedResult(String productName, int price, int promotionPriceQuantity,
                                                          int bonusQuantity, int pendingQuantity) {
        if (pendingQuantity == 0) {
            return createPromotionCompleteResult(productName, price, promotionPriceQuantity, bonusQuantity, 0);
        }
        return new BuyResult(productName, price, BuyType.PROMOTION, BuyState.PARTIALLY_PROMOTED,
                promotionPriceQuantity, bonusQuantity, pendingQuantity, 0);
    }

    public static BuyResult createBonusAddableResult(String productName, int price, int promotionPriceQuantity,
                                                     int bonusQuantity, int pendingQuantity) {
        return new BuyResult(productName, price, BuyType.PROMOTION, BuyState.BONUS_ADDABLE,
                promotionPriceQuantity, bonusQuantity, pendingQuantity, 0);
    }

    public static BuyResult createRegularCompleteResult(String productName, int price, int regularPriceQuantity) {
        return new BuyResult(productName, price, BuyType.REGULAR, BuyState.COMPLETE,
                0, 0, 0, regularPriceQuantity);
    }

    public void resolvePendingState(UserInputCommand userInputCommand) {
        if (buyState == BuyState.BONUS_ADDABLE) {
            applyBonusDecision(userInputCommand);
            return;
        }
        if (buyState == BuyState.PARTIALLY_PROMOTED) {
            applyRegularPricePaymentDecision(userInputCommand);
        }
    }

    private void applyBonusDecision(UserInputCommand bonusDecision) {
        if (bonusDecision == UserInputCommand.YES) {
            promotionPriceQuantity += pendingQuantity;
            bonusQuantity++;
        }
        if (bonusDecision == UserInputCommand.NO) {
            regularPriceQuantity = pendingQuantity;
        }
        buyState = BuyState.COMPLETE;
        pendingQuantity = 0;
    }

    private void applyRegularPricePaymentDecision(UserInputCommand regularPricePaymentDecision) {
        if (regularPricePaymentDecision == UserInputCommand.YES) {
            regularPriceQuantity = pendingQuantity;
        }
        buyState = BuyState.COMPLETE;
        pendingQuantity = 0;
    }

    public boolean hasBonus() {
        return bonusQuantity > 0;
    }

    public int getTotalBuyPrice() {
        return price * getTotalBuyQuantity();
    }

    public int getPromotionDiscountPrice() {
        return price * bonusQuantity;
    }

    public int getRegularBuyPrice() {
        return price * regularPriceQuantity;
    }

    public int getTotalBuyQuantity() {
        return promotionPriceQuantity + bonusQuantity + regularPriceQuantity;
    }

    public int getPendingQuantity() {
        return pendingQuantity;
    }

    public boolean isBonusAddable() {
        return buyState == BuyState.BONUS_ADDABLE;
    }

    public boolean isComplete() {
        return buyState == BuyState.COMPLETE;
    }

    public String getProductName() {
        return productName;
    }

    public BuyType getBuyType() {
        return buyType;
    }

    public int getBonusQuantity() {
        return bonusQuantity;
    }

}
