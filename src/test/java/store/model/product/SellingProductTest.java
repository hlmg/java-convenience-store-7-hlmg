package store.model.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class SellingProductTest {

    @Test
    void 프로모션_재고를_먼저_차감할_수_있다() {
        // given
        SellingProduct product = new SellingProduct("콜라", 1000, 10, 10, "2+1");

        // when
        product.deductPromotionStockFirst(12);

        // then
        assertThat(product).extracting("regularStock", "promotionStock")
                .containsExactly(8, 0);
    }

    @Test
    void 일반_재고를_먼저_차감할_수_있다() {
        // given
        SellingProduct product = new SellingProduct("콜라", 1000, 10, 10, "2+1");

        // when
        product.deductRegularStockFirst(12);

        // then
        assertThat(product).extracting("regularStock", "promotionStock")
                .containsExactly(0, 8);
    }

}
