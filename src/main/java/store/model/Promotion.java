package store.model;

import java.time.LocalDate;

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

    public boolean nameEquals(String name) {
        return this.name.equals(name);
    }

    public boolean isActiveOn(LocalDate date) {
        return !(date.isBefore(startDate) || date.isAfter(endDate));
    }

    public PromotionResult apply(int quantity) {
        int unit = buy + get;
        int promotionCount = quantity / unit;
        int remain = quantity % unit;
        return new PromotionResult(buy * promotionCount, get * promotionCount, remain);
    }

    public boolean isBonusApplicable(int quantity) {
        return buy == quantity;
    }

}
