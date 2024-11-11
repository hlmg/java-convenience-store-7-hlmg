package store.io.util.converter;

import java.util.Arrays;
import java.util.List;

public class StringConverter {

    private static final String DELIMITER = ",";

    public static List<String> toStringListFromCsvSource(String value) {
        return Arrays.stream(value.split(DELIMITER))
                .toList();
    }

    public static int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("숫자로 변환할 수 없습니다.");
        }
    }

}
