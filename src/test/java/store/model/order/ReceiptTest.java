package store.model.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.model.product.SellingProductSnapshot;
import store.model.promotion.PromotionResult;
import store.model.user.UserInputCommand;

@SuppressWarnings("NonAsciiCharacters")
class ReceiptTest {

    @Test
    void 총구매액을_계산할_수_있다() {
        // given
        List<BuyResult> buyResults = List.of(
                createPromotionResult("콜라", 1000, 2, 1, 4),
                createRegularResult("에너지바", 2000, 2)
        );
        Receipt receipt = new Receipt(buyResults, UserInputCommand.NO);

        // when
        int totalBuyPrice = receipt.getTotalBuyPrice();

        // then
        assertThat(totalBuyPrice).isEqualTo(11000);
    }

    @Test
    void 행사할인액을_계산할_수_있다() {
        // given
        List<BuyResult> buyResults = List.of(
                createPromotionResult("콜라", 1000, 2, 1, 4),
                createRegularResult("에너지바", 2000, 2)
        );
        Receipt receipt = new Receipt(buyResults, UserInputCommand.NO);

        // when
        int promotionDiscountPrice = receipt.getPromotionDiscountPrice();

        // then
        assertThat(promotionDiscountPrice).isEqualTo(1000);
    }

    @CsvSource(textBlock = """
            YES,2400
            NO,0
            """)
    @ParameterizedTest
    void 멤버십할인액을_계산할_수_있다(UserInputCommand membershipDecision, int expected) {
        // given
        List<BuyResult> buyResults = List.of(
                createPromotionResult("콜라", 1000, 2, 1, 4),
                createRegularResult("에너지바", 2000, 2)
        );

        Receipt receipt = new Receipt(buyResults, membershipDecision);

        // when
        int membershipDiscountPrice = receipt.getMembershipDiscountPrice();

        // then
        assertThat(membershipDiscountPrice).isEqualTo(expected);
    }

    @Test
    void 결제_금액을_계산할_수_있다() {
        // given
        List<BuyResult> buyResults = List.of(
                createPromotionResult("콜라", 1000, 2, 1, 4),
                createRegularResult("에너지바", 2000, 2)
        );

        Receipt receipt = new Receipt(buyResults, UserInputCommand.YES);

        // when
        int paymentPrice = receipt.getPaymentPrice();

        // then
        assertThat(paymentPrice).isEqualTo(7600);
    }

    private BuyResult createPromotionResult(String name, int price, int promotionQuantity, int bonusQuantity,
                                            int regularPriceQuantity) {
        return new BuyResult(
                new SellingProductSnapshot(name, price),
                new PromotionResult(promotionQuantity, bonusQuantity, 0, PromotionState.FULL_PROMOTED),
                regularPriceQuantity
        );
    }

    private BuyResult createRegularResult(String name, int price, int regularPriceQuantity) {
        return new BuyResult(
                new SellingProductSnapshot(name, price),
                null,
                regularPriceQuantity
        );
    }

}
