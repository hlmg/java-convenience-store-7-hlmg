package store.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import store.model.Product;
import store.model.Promotion;

public class ProductFileReader {

    private static final String PATH = "src/main/resources/products.md";
    private static final String DELIMITER = ",";
    private static final int INDEX_LINE = 1;
    private static final int NAME = 0;
    private static final int PRICE = 1;
    private static final int QUANTITY = 2;
    private static final int PROMOTION = 3;

    public List<Product> getProducts(List<Promotion> promotions) {
        Path path = Path.of(PATH);
        try (Stream<String> lines = Files.lines(path)) {
            return lines.skip(INDEX_LINE)
                    .map(this::toStringList)
                    .map(productInfo -> toProductData(productInfo, promotions))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("상품 정보를 가져올 수 없습니다.");
        }
    }

    private List<String> toStringList(String value) {
        return Arrays.stream(value.split(DELIMITER))
                .toList();
    }

    private Product toProductData(List<String> productInfo, List<Promotion> promotions) {
        Promotion promotion = findPromotion(promotions, productInfo.get(PROMOTION));
        return new Product(
                productInfo.get(NAME),
                toInt(productInfo.get(PRICE)),
                toInt(productInfo.get(QUANTITY)),
                promotion);
    }

    private Promotion findPromotion(List<Promotion> promotions, String promotionName) {
        return promotions.stream()
                .filter(promotion -> promotion.isSameName(promotionName))
                .findAny()
                .orElse(null);
    }

    private int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

}
