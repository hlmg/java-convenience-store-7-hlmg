package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProductInventoryTest {

    @Test
    void 이름으로_상품을_조회할_수_있다() {
        // given
        String productName = "콜라";
        Promotion promotion = createPromotion("2+1");
        ProductInventory productInventory = new ProductInventory(List.of(
                new Product(productName, 1000, 10, promotion),
                new Product(productName, 1000, 5, null),
                new Product("에너지바", 2000, 5, null))
        );

        // when
        List<Product> findProducts = productInventory.findByName(productName);

        // then
        assertThat(findProducts).hasSize(2)
                .extracting("name", "quantity")
                .containsExactlyInAnyOrder(
                        tuple(productName, 10),
                        tuple(productName, 5)
                );
    }

    @Test
    void 없는_상품을_조회하면_예외가_발생한다() {
        // given
        ProductInventory productInventory = new ProductInventory(List.of(
                new Product("콜라", 1000, 10, null),
                new Product("에너지바", 2000, 5, null))
        );

        // when & then
        assertThatThrownBy(() -> productInventory.findByName("우테코"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    Promotion createPromotion(String promotionName) {
        return new Promotion(
                promotionName,
                2,
                1,
                LocalDate.parse("2024-11-01"),
                LocalDate.parse("2024-11-30"));
    }

}
