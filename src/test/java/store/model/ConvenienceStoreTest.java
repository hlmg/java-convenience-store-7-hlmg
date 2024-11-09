package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ConvenienceStoreTest {

    // TODO: 상품 주문 기능 구현 후 수정하기
    @Disabled
    @Test
    void 주문한_상품이_구매_가능한지_확인할_수_있다() {
        // given
        List<Product> products = List.of(new Product("콜라", 1000, 10, null));
        ConvenienceStore convenienceStore = new ConvenienceStore(products, List.of());

        OrderProduct orderProduct = new OrderProduct("콜라", 10);
        LocalDate orderDate = LocalDate.parse("2024-11-01");

        // when
//        boolean result = convenienceStore.buy(orderProduct, orderDate);

        // then
//        assertThat(result).isTrue();
    }

    @Test
    void 없는_상품을_구매하면_예외가_발생한다() {
        // given
        List<Product> products = List.of(new Product("콜라", 1000, 10, null));
        ConvenienceStore convenienceStore = new ConvenienceStore(products, List.of());

        OrderProduct orderProduct = new OrderProduct("에너지바", 10);
        LocalDate orderDate = LocalDate.parse("2024-11-01");

        // when & then
        assertThatThrownBy(() -> convenienceStore.buy(orderProduct, orderDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    void 재고보다_많은_수량을_구매하면_예외가_발생한다() {
        // given
        List<Product> products = List.of(new Product("콜라", 1000, 10, null));
        ConvenienceStore convenienceStore = new ConvenienceStore(products, List.of());

        OrderProduct orderProduct = new OrderProduct("콜라", 11);
        LocalDate orderDate = LocalDate.parse("2024-11-01");

        // when & then
        assertThatThrownBy(() -> convenienceStore.buy(orderProduct, orderDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고 수량을 초과하여 구매할 수 없습니다.");
    }

}
