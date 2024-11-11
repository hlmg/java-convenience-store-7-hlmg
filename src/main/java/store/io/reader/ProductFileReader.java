package store.io.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import store.io.util.converter.StringConverter;
import store.model.product.Product;

public class ProductFileReader {

    private static final String PATH = "src/main/resources/products.md";
    private static final int INDEX_LINE = 1;
    private static final int NAME = 0;
    private static final int PRICE = 1;
    private static final int QUANTITY = 2;
    private static final int PROMOTION = 3;
    private static final String NULL_STRING = "null";

    public List<Product> getProducts() {
        Path path = Path.of(PATH);
        try (Stream<String> lines = Files.lines(path)) {
            return lines.skip(INDEX_LINE)
                    .map(StringConverter::toStringListFromCsvSource)
                    .map(this::toProduct)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("상품 정보를 가져올 수 없습니다.");
        }
    }

    private Product toProduct(List<String> product) {
        return new Product(
                product.get(NAME),
                StringConverter.toInt(product.get(PRICE)),
                StringConverter.toInt(product.get(QUANTITY)),
                getPromotion(product.get(PROMOTION))
        );
    }

    private String getPromotion(String promotion) {
        if (promotion.equals(NULL_STRING)) {
            return null;
        }
        return promotion;
    }

}
