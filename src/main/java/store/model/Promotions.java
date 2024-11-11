package store.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Promotions {

    private final Map<String, Promotion> promotions = new HashMap<>();

    public Promotions(List<Promotion> promotions) {
        for (Promotion promotion : promotions) {
            this.promotions.putIfAbsent(promotion.getName(), promotion);
        }
    }

    public Optional<Promotion> findActivePromotion(String name, LocalDate orderDate) {
        Promotion promotion = promotions.get(name);
        if (promotion == null || promotion.isDeActiveOn(orderDate)) {
            return Optional.empty();
        }
        return Optional.of(promotion);
    }

}
