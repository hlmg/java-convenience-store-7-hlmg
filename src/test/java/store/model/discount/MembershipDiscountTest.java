package store.model.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.model.discount.MembershipDiscount;

@SuppressWarnings("NonAsciiCharacters")
class MembershipDiscountTest {

    @CsvSource(textBlock = """
            10000,3000
            26640,7990
            26650,8000
            30000,8000
            """)
    @ParameterizedTest
    void 할인액을_계산할_수_있다(int price, int expected) {
        // given
        MembershipDiscount membershipDiscount = new MembershipDiscount();

        // when
        int discountAmount = membershipDiscount.getDiscountAmount(price);

        // then
        assertThat(discountAmount).isEqualTo(expected);
    }

}
