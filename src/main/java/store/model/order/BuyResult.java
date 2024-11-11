package store.model.order;

import store.model.user.UserInputCommand;

public record BuyResult(
        String productName,
        int price,
        BuyType buyType,
        BuyState buyState,
        int promotionPriceQuantity,
        int bonusQuantity,
        int pendingQuantity,
        int regularPriceQuantity
) {

    public static BuyResult createPromotionCompleteResult(String productName, int price, int promotionPriceQuantity,
                                                          int bonusQuantity,
                                                          int regularPriceQuantity) {
        return new BuyResult(productName, price, BuyType.PROMOTION, BuyState.COMPLETE,
                promotionPriceQuantity, bonusQuantity, 0, regularPriceQuantity);
    }

    public static BuyResult createPartiallyPromotedResult(String productName, int price, int promotionPriceQuantity,
                                                          int bonusQuantity,
                                                          int pendingQuantity) {
        if (pendingQuantity == 0) {
            return createPromotionCompleteResult(productName, price, promotionPriceQuantity, bonusQuantity, 0);
        }
        return new BuyResult(productName, price, BuyType.PROMOTION, BuyState.PARTIALLY_PROMOTED,
                promotionPriceQuantity, bonusQuantity, pendingQuantity, 0);
    }

    public static BuyResult createBonusAddableResult(String productName, int price, int promotionPriceQuantity,
                                                     int bonusQuantity,
                                                     int pendingQuantity) {
        return new BuyResult(productName, price, BuyType.PROMOTION, BuyState.BONUS_ADDABLE,
                promotionPriceQuantity, bonusQuantity, pendingQuantity, 0);
    }

    public static BuyResult createRegularCompleteResult(String productName, int price, int regularPriceQuantity) {
        return new BuyResult(productName, price, BuyType.REGULAR, BuyState.COMPLETE,
                0, 0, 0, regularPriceQuantity);
    }

    public BuyResult applyBonusDecision(UserInputCommand bonusDecision) {
        checkBonusAddableType();
        checkBonusAddableState();
        if (bonusDecision == UserInputCommand.YES) {
            int promotionPriceQuantity = this.promotionPriceQuantity + pendingQuantity;
            int bonusQuantity = this.bonusQuantity + 1;
            return createPromotionCompleteResult(productName, price, promotionPriceQuantity, bonusQuantity, 0);
        }
        if (bonusDecision == UserInputCommand.NO) {
            return createPromotionCompleteResult(productName, price, promotionPriceQuantity, bonusQuantity,
                    pendingQuantity);
        }
        throw new IllegalStateException("지원하지 않는 명령입니다.");
    }

    public BuyResult applyRegularPricePaymentDecision(UserInputCommand regularPricePaymentDecision) {
        checkRegularPricePaymentType();
        checkRegularPricePaymentState();
        if (regularPricePaymentDecision == UserInputCommand.YES) {
            return createPromotionCompleteResult(productName, price, promotionPriceQuantity, bonusQuantity,
                    pendingQuantity);
        }
        if (regularPricePaymentDecision == UserInputCommand.NO) {
            return createPromotionCompleteResult(productName, price, promotionPriceQuantity, bonusQuantity, 0);
        }
        throw new IllegalStateException("지원하지 않는 명령입니다.");
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

    private void checkBonusAddableType() {
        if (this.buyType != BuyType.PROMOTION) {
            throw new IllegalStateException("증정 상품 추가 여부를 처리할 수 없는 타입입니다.");
        }
    }

    private void checkBonusAddableState() {
        if (this.buyState != BuyState.BONUS_ADDABLE) {
            throw new IllegalStateException("증정 상품 추가 여부를 처리할 수 없는 상태입니다.");
        }
    }

    private void checkRegularPricePaymentType() {
        if (this.buyType != BuyType.PROMOTION) {
            throw new IllegalStateException("정가 결제 여부를 처리할 수 없는 타입입니다.");
        }
    }

    private void checkRegularPricePaymentState() {
        if (this.buyState != BuyState.PARTIALLY_PROMOTED) {
            throw new IllegalStateException("정가 결제 여부를 처리할 수 없는 상태입니다.");
        }
    }

}
