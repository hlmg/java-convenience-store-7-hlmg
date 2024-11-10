package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class BuyResultTest {

    static final String PRODUCT_NAME = "콜라";
    static final int PRICE = 1000;


    @CsvSource(textBlock = """
            YES,COMPLETE,6,3,0,0
            NO,COMPLETE,4,2,0,2
            """)
    @ParameterizedTest
    void 증정_상품_추가_여부를_처리할_수_있다(UserInputCommand userInputCommand, BuyState buyState, int promotionPriceQuantity,
                               int bonusQuantity, int pendingQuantity, int regularPriceQuantity) {
        // given
        BuyResult buyResult = new BuyResult(PRODUCT_NAME, PRICE, BuyType.PROMOTION, BuyState.BONUS_ADDABLE, 4, 2, 2, 0);

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
        BuyResult buyResult = new BuyResult(PRODUCT_NAME, PRICE, BuyType.REGULAR, BuyState.BONUS_ADDABLE, 4, 2, 2, 0);
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
        BuyResult buyResult = new BuyResult(PRODUCT_NAME, PRICE, BuyType.PROMOTION, buyState, 4, 2, 2, 0);
        UserInputCommand userInputCommand = UserInputCommand.YES;

        // when & then
        assertThatThrownBy(() -> buyResult.applyBonusDecision(userInputCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("증정 상품 추가 여부를 처리할 수 없는 상태입니다.");
    }

    @CsvSource(textBlock = """
            YES,COMPLETE,4,2,0,2
            NO,COMPLETE,4,2,0,0
            """)
    @ParameterizedTest
    void 정가_결제_여부를_처리할_수_있다(UserInputCommand userInputCommand, BuyState buyState, int promotionPriceQuantity,
                            int bonusQuantity, int pendingQuantity, int regularPriceQuantity) {
        // given
        BuyResult buyResult = new BuyResult(PRODUCT_NAME, PRICE, BuyType.PROMOTION, BuyState.PARTIALLY_PROMOTED, 4, 2,
                2, 0);

        // when
        BuyResult finalResult = buyResult.applyRegularPricePaymentDecision(userInputCommand);

        // then
        assertThat(finalResult.buyState()).isSameAs(buyState);
        assertThat(finalResult.promotionPriceQuantity()).isSameAs(promotionPriceQuantity);
        assertThat(finalResult.bonusQuantity()).isSameAs(bonusQuantity);
        assertThat(finalResult.pendingQuantity()).isSameAs(pendingQuantity);
        assertThat(finalResult.regularPriceQuantity()).isSameAs(regularPriceQuantity);
    }

    @Test
    void 정가_결제_여부를_처리할_수_없는_타입이면_예외가_발생한다() {
        // given
        BuyResult buyResult = new BuyResult(PRODUCT_NAME, PRICE, BuyType.REGULAR, BuyState.PARTIALLY_PROMOTED, 4, 2, 2,
                0);
        UserInputCommand userInputCommand = UserInputCommand.YES;

        // when & then
        assertThatThrownBy(() -> buyResult.applyRegularPricePaymentDecision(userInputCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("정가 결제 여부를 처리할 수 없는 타입입니다.");
    }

    @ValueSource(strings = {"COMPLETE", "BONUS_ADDABLE"})
    @ParameterizedTest
    void 정가_결제_여부를_처리할_수_없는_상태면_예외가_발생한다(BuyState buyState) {
        // given
        BuyResult buyResult = new BuyResult(PRODUCT_NAME, PRICE, BuyType.PROMOTION, buyState, 4, 2, 2, 0);
        UserInputCommand userInputCommand = UserInputCommand.YES;

        // when & then
        assertThatThrownBy(() -> buyResult.applyRegularPricePaymentDecision(userInputCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("정가 결제 여부를 처리할 수 없는 상태입니다.");
    }

    @Test
    void 총_구매액을_계산할_수_있다() {
        // given
        int price = 1000;
        BuyResult buyResult = new BuyResult(PRODUCT_NAME, price, BuyType.PROMOTION, BuyState.COMPLETE, 4, 2,
                0, 2);

        // when
        int totalBuyPrice = buyResult.getTotalBuyPrice();

        // then
        assertThat(totalBuyPrice).isEqualTo(8000);
    }

    @Test
    void 행사할인_금액을_계산할_수_있다() {
        // given
        int price = 1000;
        int bonusQuantity = 2;
        BuyResult buyResult = new BuyResult(PRODUCT_NAME, price, BuyType.PROMOTION, BuyState.COMPLETE, 4, bonusQuantity,
                0, 3);

        // when
        int promotionDiscountPrice = buyResult.getPromotionDiscountPrice();

        // then
        assertThat(promotionDiscountPrice).isEqualTo(2000);
    }

    @Test
    void 정가로_구매한_금액을_계산할_수_있다() {
        // given
        int price = 1000;
        int regularPriceQuantity = 3;
        BuyResult buyResult = new BuyResult(PRODUCT_NAME, price, BuyType.PROMOTION, BuyState.COMPLETE,
                4, 2, 0, regularPriceQuantity);

        // when
        int paymentPrice = buyResult.getRegularBuyPrice();

        // then
        assertThat(paymentPrice).isEqualTo(3000);
    }

    @Test
    void 총_구매_수량을_계산할_수_있다() {
        // given
        int promotionPriceQuantity = 4;
        int bonusQuantity = 2;
        int regularPriceQuantity = 3;
        BuyResult buyResult = new BuyResult(PRODUCT_NAME, 1000, BuyType.PROMOTION, BuyState.COMPLETE,
                promotionPriceQuantity, bonusQuantity, 0, regularPriceQuantity);

        // when
        int totalBuyQuantity = buyResult.getTotalBuyQuantity();

        // then
        assertThat(totalBuyQuantity).isEqualTo(9);
    }

}
