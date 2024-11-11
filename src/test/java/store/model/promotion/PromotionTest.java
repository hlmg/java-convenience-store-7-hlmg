package store.model.promotion;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class PromotionTest {

    @Test
    void 프로모션을_적용할_수_있다() {
        // given
        Promotion promotion = new Promotion("2+1", 2, 1,
                LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"));
        int promotionStock = 20;
        int orderQuantity = 10;

        // when
        PromotionResult promotionResult = promotion.apply(promotionStock, orderQuantity);

        // then
        assertThat(promotionResult.getPromotionPriceQuantity()).isEqualTo(6);
        assertThat(promotionResult.getBonusQuantity()).isEqualTo(3);
        assertThat(promotionResult.getPendingQuantity()).isEqualTo(1);
    }

    @CsvSource(textBlock = """
            2024-10-31,true
            2024-11-01,false
            2024-11-30,false
            2024-12-01,true
            """)
    @ParameterizedTest
    void 특정일에_프로모션이_진행중이_아닌지_확인할_수_있다(String date, boolean expected) {
        // given
        LocalDate localDate = LocalDate.parse(date);
        Promotion promotion = new Promotion("2+1", 2, 1,
                LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"));

        // when
        boolean actual = promotion.isDeActiveOn(localDate);

        // then
        assertThat(actual).isEqualTo(expected);
    }

}
