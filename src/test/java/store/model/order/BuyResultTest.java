package store.model.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.model.product.SellingProductSnapshot;
import store.model.promotion.PromotionResult;
import store.model.user.UserInputCommand;

@SuppressWarnings("NonAsciiCharacters")
class BuyResultTest {

    static final String PRODUCT_NAME = "콜라";
    static final int PRICE = 1000;

    @CsvSource(textBlock = """
            YES,9,3,0
            NO,8,2,0
            """)
    @ParameterizedTest
    void 증정_상품_추가_여부를_처리할_수_있다(UserInputCommand userInputCommand, int totalBuyQuantity, int bonusQuantity,
                               int pendingQuantity) {
        // given
        SellingProductSnapshot sellingProduct = new SellingProductSnapshot(PRODUCT_NAME, PRICE);
        PromotionResult promotionResult = new PromotionResult(4, 2, 2, PromotionState.BONUS_ADDABLE);
        BuyResult buyResult = BuyResult.createPromotionResult(sellingProduct, promotionResult);

        // when
        buyResult.resolvePendingState(userInputCommand);

        // then
        assertThat(buyResult.isComplete()).isTrue();
        assertThat(buyResult.getTotalBuyQuantity()).isEqualTo(totalBuyQuantity);
        assertThat(buyResult.getBonusQuantity()).isEqualTo(bonusQuantity);
        assertThat(buyResult.getPendingQuantity()).isEqualTo(pendingQuantity);
    }

    @CsvSource(textBlock = """
            YES,8,2,0
            NO,6,2,0
            """)
    @ParameterizedTest
    void 정가_결제_여부를_처리할_수_있다(UserInputCommand userInputCommand, int totalBuyQuantity, int bonusQuantity,
                            int pendingQuantity) {
        // given
        SellingProductSnapshot sellingProduct = new SellingProductSnapshot(PRODUCT_NAME, PRICE);
        PromotionResult promotionResult = new PromotionResult(4, 2, 2, PromotionState.PARTIALLY_PROMOTED);
        BuyResult buyResult = BuyResult.createPromotionResult(sellingProduct, promotionResult);

        // when
        buyResult.resolvePendingState(userInputCommand);

        // then
        assertThat(buyResult.isComplete()).isTrue();
        assertThat(buyResult.getTotalBuyQuantity()).isEqualTo(totalBuyQuantity);
        assertThat(buyResult.getBonusQuantity()).isEqualTo(bonusQuantity);
        assertThat(buyResult.getPendingQuantity()).isEqualTo(pendingQuantity);
    }

    @Test
    void 총_구매액을_계산할_수_있다() {
        // given
        int price = 1000;
        SellingProductSnapshot sellingProduct = new SellingProductSnapshot(PRODUCT_NAME, price);
        PromotionResult promotionResult = new PromotionResult(4, 2, 0, PromotionState.FULL_PROMOTED);
        BuyResult buyResult = new BuyResult(sellingProduct, promotionResult, 2);

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
        SellingProductSnapshot sellingProduct = new SellingProductSnapshot(PRODUCT_NAME, price);
        PromotionResult promotionResult = new PromotionResult(4, bonusQuantity, 0, PromotionState.FULL_PROMOTED);
        BuyResult buyResult = new BuyResult(sellingProduct, promotionResult, 3);

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
        SellingProductSnapshot sellingProduct = new SellingProductSnapshot(PRODUCT_NAME, price);
        PromotionResult promotionResult = new PromotionResult(4, 2, 0, PromotionState.FULL_PROMOTED);
        BuyResult buyResult = new BuyResult(sellingProduct, promotionResult, regularPriceQuantity);

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

        SellingProductSnapshot sellingProduct = new SellingProductSnapshot(PRODUCT_NAME, PRICE);
        PromotionResult promotionResult = new PromotionResult(promotionPriceQuantity, bonusQuantity, 0,
                PromotionState.FULL_PROMOTED);
        BuyResult buyResult = new BuyResult(sellingProduct, promotionResult, regularPriceQuantity);

        // when
        int totalBuyQuantity = buyResult.getTotalBuyQuantity();

        // then
        assertThat(totalBuyQuantity).isEqualTo(9);
    }

}
