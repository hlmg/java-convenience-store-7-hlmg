package store.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import store.model.Promotion;

public class PromotionFileReader {

    private static final String PATH = "src/main/resources/promotions.md";
    private static final String DELIMITER = ",";
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

    private Promotion toPromotion(List<String> strings) {
        return new Promotion(
                strings.get(NAME),
                toInt(strings.get(BUY)),
                toInt(strings.get(GET)),
                toLocalDate(strings.get(START_DATE)),
                toLocalDate(strings.get(END_DATE))
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
