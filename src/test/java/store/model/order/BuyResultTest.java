package store.model.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.model.user.UserInputCommand;

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
        buyResult.resolvePendingState(userInputCommand);

        // then
        assertThat(buyResult).extracting("buyState", "promotionPriceQuantity", "bonusQuantity", "pendingQuantity",
                        "regularPriceQuantity")
                .contains(buyState, promotionPriceQuantity, bonusQuantity, pendingQuantity, regularPriceQuantity);
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
        buyResult.resolvePendingState(userInputCommand);

        // then
        assertThat(buyResult).extracting("buyState", "promotionPriceQuantity", "bonusQuantity", "pendingQuantity",
                        "regularPriceQuantity")
                .contains(buyState, promotionPriceQuantity, bonusQuantity, pendingQuantity, regularPriceQuantity);
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
