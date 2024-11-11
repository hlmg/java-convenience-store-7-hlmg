package store.io.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import store.io.util.converter.StringConverter;
import store.model.promotion.Promotion;

public class PromotionFileReader {

    private static final String PATH = "src/main/resources/promotions.md";
    private static final int INDEX_LINE = 1;
    private static final int NAME = 0;
    private static final int BUY = 1;
    private static final int GET = 2;
    private static final int START_DATE = 3;
    private static final int END_DATE = 4;

    public List<Promotion> getPromotions() {
        Path path = Path.of(PATH);
        try (Stream<String> lines = Files.lines(path)) {
            return lines.skip(INDEX_LINE)
                    .map(StringConverter::toStringListFromCsvSource)
                    .map(this::toPromotion)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("프로모션 정보를 가져올 수 없습니다.");
        }
    }

    private Promotion toPromotion(List<String> promotion) {
        return new Promotion(
                promotion.get(NAME),
                StringConverter.toInt(promotion.get(BUY)),
                StringConverter.toInt(promotion.get(GET)),
                LocalDate.parse(promotion.get(START_DATE)),
                LocalDate.parse(promotion.get(END_DATE))
        );
    }

}
