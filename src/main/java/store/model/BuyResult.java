package store.model;

public record BuyResult(
        BuyType buyType,
        BuyState buyState,
        int promotionPriceQuantity,
        int bonusQuantity,
        int pendingQuantity,
        int regularPriceQuantity
) {

    public static BuyResult createPromotionCompleteResult(int promotionPriceQuantity, int bonusQuantity,
                                                          int regularPriceQuantity) {
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

    public BuyResult applyBonusDecision(UserInputCommand userInputCommand) {
        checkBonusAddableType();
        checkBonusAddableState();
        if (userInputCommand == UserInputCommand.YES) {
            int promotionPriceQuantity = this.promotionPriceQuantity + pendingQuantity;
            int bonusQuantity = this.bonusQuantity + 1;
            return createPromotionCompleteResult(promotionPriceQuantity, bonusQuantity, 0);
        }
        if (userInputCommand == UserInputCommand.NO) {
            return createPromotionCompleteResult(promotionPriceQuantity, bonusQuantity, pendingQuantity);
        }
        throw new IllegalStateException("지원하지 않는 명령입니다.");
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

}
