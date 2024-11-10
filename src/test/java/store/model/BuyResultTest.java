package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class BuyResultTest {

    @CsvSource(textBlock = """
            YES,COMPLETE,6,3,0,0
            NO,COMPLETE,4,2,0,2
            """)
    @ParameterizedTest
    void 증정_상품_추가_여부를_처리할_수_있다(UserInputCommand userInputCommand, BuyState buyState, int promotionPriceQuantity,
                               int bonusQuantity, int pendingQuantity, int regularPriceQuantity) {
        // given
        BuyResult buyResult = new BuyResult(BuyType.PROMOTION, BuyState.BONUS_ADDABLE, 4, 2, 2, 0);

        // when
        BuyResult finalResult = buyResult.applyBonusDecision(userInputCommand);

        // then
        assertThat(finalResult.buyState()).isSameAs(buyState);
        assertThat(finalResult.promotionPriceQuantity()).isSameAs(promotionPriceQuantity);
        assertThat(finalResult.bonusQuantity()).isSameAs(bonusQuantity);
        assertThat(finalResult.pendingQuantity()).isSameAs(pendingQuantity);
        assertThat(finalResult.regularPriceQuantity()).isSameAs(regularPriceQuantity);
    }

    @Test
    void 증정_상품을_추가_여부를_처리할_수_없는_타입이면_예외가_발생한다() {
        // given
        BuyResult buyResult = new BuyResult(BuyType.REGULAR, BuyState.BONUS_ADDABLE, 4, 2, 2, 0);
        UserInputCommand userInputCommand = UserInputCommand.YES;

        // when & then
        assertThatThrownBy(() -> buyResult.applyBonusDecision(userInputCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("증정 상품 추가 여부를 처리할 수 없는 타입입니다.");
    }

    @ValueSource(strings = {"COMPLETE", "PARTIALLY_PROMOTED"})
    @ParameterizedTest
    void 증정_상품을_추가_여부를_처리할_수_없는_상태면_예외가_발생한다(BuyState buyState) {
        // given
        BuyResult buyResult = new BuyResult(BuyType.PROMOTION, buyState, 4, 2, 2, 0);
        UserInputCommand userInputCommand = UserInputCommand.YES;

        // when & then
        assertThatThrownBy(() -> buyResult.applyBonusDecision(userInputCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("증정 상품 추가 여부를 처리할 수 없는 상태입니다.");
    }

}
