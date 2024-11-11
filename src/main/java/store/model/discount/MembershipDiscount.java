package store.model.discount;

public class MembershipDiscount {

    private static final int DISCOUNT_LIMIT = 8000;
    private static final double DISCOUNT_RATE = 0.3;
    private static final double ROUNDING_UNIT = 10.0;

    public int getDiscountAmount(int price) {
        double discountPrice = price * DISCOUNT_RATE;
        discountPrice = roundToUnit(discountPrice);
        return (int) Math.min(DISCOUNT_LIMIT, discountPrice);
    }

    private double roundToUnit(double discountPrice) {
        return Math.round(discountPrice / ROUNDING_UNIT) * ROUNDING_UNIT;
    }

}
