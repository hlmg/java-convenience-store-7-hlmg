package store.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class PromotionTest {

    @Test
    void 프로모션을_적용할_수_있다() {
        // given
        Promotion promotion = new Promotion("2+1", 2, 1,
                LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"));
        int quantity = 10;

        // when
        PromotionResult promotionResult = promotion.apply(quantity);

        // then
        assertThat(promotionResult.buy()).isEqualTo(6);
        assertThat(promotionResult.get()).isEqualTo(3);
        assertThat(promotionResult.remain()).isEqualTo(1);
    }

    // TODO: 테스트 구현하기.
    @Test
    void 특정일에_프로모션이_진행중인지_확인할_수_있다() {
    }

}
