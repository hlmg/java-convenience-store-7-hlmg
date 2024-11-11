package store.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import store.model.promotion.Promotion;

public class PromotionFileReader {

    private static final String PATH = "src/main/resources/promotions.md";
    private static final String DELIMITER = ",";

    public List<Promotion> getPromotions() {
        Path path = Path.of(PATH);
        try (Stream<String> lines = Files.lines(path)) {
            return lines.skip(1)
                    .map(this::toStringList)
                    .map(this::toPromotion)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("프로모션 정보를 가져올 수 없습니다.");
        }
    }

    private List<String> toStringList(String value) {
        return Arrays.stream(value.split(DELIMITER))
                .toList();
    }

    private Promotion toPromotion(List<String> promotion) {
        return new Promotion(
                promotion.get(0),
                toInt(promotion.get(1)),
                toInt(promotion.get(2)),
                toLocalDate(promotion.get(3)),
                toLocalDate(promotion.get(4))
        );
    }

    private int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDate toLocalDate(String s) {
        return LocalDate.parse(s);
    }

}
