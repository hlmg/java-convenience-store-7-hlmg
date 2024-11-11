package store.model.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.Test;
import store.model.order.OrderOrganizer;
import store.model.order.OrderProduct;

@SuppressWarnings("NonAsciiCharacters")
class OrderOrganizerTest {

    OrderOrganizer orderOrganizer = new OrderOrganizer();

    @Test
    void 중복_주문상품의_수량이_합산된_리스트를_구할_수_있다() {
        // given
        List<OrderProduct> orderProducts = List.of(
                new OrderProduct("콜라", 1),
                new OrderProduct("에너지바", 1),
                new OrderProduct("콜라", 3)
        );

        // when
        List<OrderProduct> organizedOrderProducts = orderOrganizer.organizeOrderProducts(orderProducts);

        // then
        assertThat(organizedOrderProducts).hasSize(2)
                .extracting("name", "quantity")
                .containsExactly(
                        tuple("콜라", 4),
                        tuple("에너지바", 1)
                );
    }

}
