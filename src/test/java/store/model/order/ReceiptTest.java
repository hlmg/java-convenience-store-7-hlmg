package store.model.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.model.order.BuyResult;
import store.model.order.BuyState;
import store.model.order.BuyType;
import store.model.order.Receipt;
import store.model.user.UserInputCommand;

@SuppressWarnings("NonAsciiCharacters")
class ReceiptTest {

    @Test
    void 총구매액을_계산할_수_있다() {
        // given
        List<BuyResult> buyResults = List.of(
                new BuyResult("콜라", 1000, BuyType.PROMOTION, BuyState.COMPLETE,
                        2, 1, 0, 4),
                new BuyResult("에너지바", 2000, BuyType.REGULAR, BuyState.COMPLETE,
                        0, 0, 0, 2)
        );
        Receipt receipt = new Receipt(buyResults);

        // when
        int totalBuyPrice = receipt.getTotalBuyPrice();

        // then
        assertThat(totalBuyPrice).isEqualTo(11000);
    }

    @Test
    void 행사할인액을_계산할_수_있다() {
        // given
        List<BuyResult> buyResults = List.of(
                new BuyResult("콜라", 1000, BuyType.PROMOTION, BuyState.COMPLETE,
                        2, 1, 0, 4),
                new BuyResult("에너지바", 2000, BuyType.REGULAR, BuyState.COMPLETE,
                        0, 0, 0, 2)
        );
        Receipt receipt = new Receipt(buyResults);

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
                new BuyResult("콜라", 1000, BuyType.PROMOTION, BuyState.COMPLETE,
                        2, 1, 0, 4),
                new BuyResult("에너지바", 2000, BuyType.REGULAR, BuyState.COMPLETE,
                        0, 0, 0, 2)
        );
        Receipt receipt = new Receipt(buyResults);
        receipt.updateMembershipDecision(membershipDecision);

        // when
        int membershipDiscountPrice = receipt.getMembershipDiscountPrice();

        // then
        assertThat(membershipDiscountPrice).isEqualTo(expected);
    }

    @Test
    void 결제_금액을_계산할_수_있다() {
        // given
        List<BuyResult> buyResults = List.of(
                new BuyResult("콜라", 1000, BuyType.PROMOTION, BuyState.COMPLETE,
                        2, 1, 0, 4),
                new BuyResult("에너지바", 2000, BuyType.REGULAR, BuyState.COMPLETE,
                        0, 0, 0, 2)
        );
        Receipt receipt = new Receipt(buyResults);
        receipt.updateMembershipDecision(UserInputCommand.YES);

        // when
        int paymentPrice = receipt.getPaymentPrice();

        // then
        assertThat(paymentPrice).isEqualTo(7600);
    }

}
