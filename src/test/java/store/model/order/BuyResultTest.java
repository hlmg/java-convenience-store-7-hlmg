package store.model.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import store.model.product.SellingProductSnapshot;
import store.model.promotion.PromotionResult;

@SuppressWarnings("NonAsciiCharacters")
class BuyResultTest {

    @Test
    void 총_구매액을_계산할_수_있다() {
        // given
        int price = 1000;
        SellingProductSnapshot sellingProduct = createProductByPrice(price);
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
        SellingProductSnapshot sellingProduct = createProductByPrice(price);
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
        SellingProductSnapshot sellingProduct = createProductByPrice(price);
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

        SellingProductSnapshot sellingProduct = createProductByPrice(1000);
        PromotionResult promotionResult = new PromotionResult(promotionPriceQuantity, bonusQuantity, 0,
                PromotionState.FULL_PROMOTED);
        BuyResult buyResult = new BuyResult(sellingProduct, promotionResult, regularPriceQuantity);

        // when
        int totalBuyQuantity = buyResult.getTotalBuyQuantity();

        // then
        assertThat(totalBuyQuantity).isEqualTo(9);
    }

    private SellingProductSnapshot createProductByPrice(int price) {
        return new SellingProductSnapshot("콜라", price, 10, 10, "2+1");
    }

}
