package store.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import store.model.Product;

public class ProductFileReader {

    private static final String PATH = "src/main/resources/products.md";
    private static final String DELIMITER = ",";

    public List<Product> getProducts() {
        Path path = Path.of(PATH);
        try (Stream<String> lines = Files.lines(path)) {
            return lines.skip(1)
                    .map(this::toStringList)
                    .map(this::toProduct)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("상품 정보를 가져올 수 없습니다.");
        }
    }

    private List<String> toStringList(String value) {
        return Arrays.stream(value.split(DELIMITER))
                .toList();
    }

    private Product toProduct(List<String> product) {
        return new Product(
                product.get(0),
                toInt(product.get(1)),
                toInt(product.get(2)),
                getPromotion(product.get(3))
        );
    }

    private String getPromotion(String promotion) {
        if (promotion.equals("null")) {
            return null;
        }
        return promotion;
    }

    private int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

}
