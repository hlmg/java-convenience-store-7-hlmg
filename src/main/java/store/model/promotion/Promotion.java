package store.model.promotion;

import java.time.LocalDate;
import store.model.order.PromotionState;

public class Promotion {

    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isDeActiveOn(LocalDate date) {
        return date.isBefore(startDate) || date.isAfter(endDate);
    }

    public PromotionResult apply(int stock, int orderQuantity) {
        int promotionApplyQuantity = Math.min(stock, orderQuantity);
        int unit = buy + get;
        int promotionCount = promotionApplyQuantity / unit;
        int remain = promotionApplyQuantity % unit;
        if (stock < orderQuantity) {
            remain += orderQuantity - stock;
        }
        PromotionState promotionState = getPromotionState(stock, orderQuantity, remain);
        return new PromotionResult(buy * promotionCount, get * promotionCount, remain, promotionState);
    }

    private PromotionState getPromotionState(int stock, int orderQuantity, int remain) {
        if (stock < orderQuantity) {
            return PromotionState.PARTIALLY_PROMOTED;
        }
        if (stock == orderQuantity && remain == get) {
            return PromotionState.PARTIALLY_PROMOTED;
        }
        return getPromotionState(remain);
    }

    private PromotionState getPromotionState(int remain) {
        if (remain == 0) {
            return PromotionState.FULL_PROMOTED;
        }
        if (remain == buy) {
            return PromotionState.BONUS_ADDABLE;
        }
        return PromotionState.PARTIALLY_PROMOTED;
    }

    public String getName() {
        return name;
    }

}
