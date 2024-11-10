package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ConvenienceStoreTest {

    @Nested
    @DisplayName("프로모션 재고가 부족하거나 구매 수량과 같은 경우")
    class promotionLowStock {

        @Test
        void 부분_적용되었는지_확인할_수_있다() {
            // given
            List<Promotion> promotions = List.of(
                    new Promotion("2+1", 2, 1,
                            LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"))
            );
            List<Product> products = List.of(
                    new Product("콜라", 1000, 6, "2+1"),
                    new Product("콜라", 1000, 10, null)
            );
            ConvenienceStore convenienceStore = new ConvenienceStore(products, promotions);
            OrderProduct orderProduct = new OrderProduct("콜라", 8);
            LocalDate orderDate = LocalDate.parse("2024-11-01");

            // when
            BuyResult buyResult = convenienceStore.buy(orderProduct, orderDate);

            // then
            assertThat(buyResult.getBuyType()).isSameAs(BuyType.PROMOTION);
            assertThat(buyResult.getBuyState()).isSameAs(BuyState.PARTIALLY_PROMOTED);
            assertThat(buyResult.getPromotionPriceQuantity()).isEqualTo(4);
            assertThat(buyResult.getBonusQuantity()).isEqualTo(2);
            assertThat(buyResult.getPendingQuantity()).isEqualTo(2);
            assertThat(buyResult.getRegularPriceQuantity()).isEqualTo(0);
        }

        @Test
        void 전체_적용되었는지_확인할_수_있다() {
            // given
            List<Promotion> promotions = List.of(
                    new Promotion("2+1", 2, 1,
                            LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"))
            );
            List<Product> products = List.of(
                    new Product("콜라", 1000, 6, "2+1"),
                    new Product("콜라", 1000, 10, null)
            );
            ConvenienceStore convenienceStore = new ConvenienceStore(products, promotions);
            OrderProduct orderProduct = new OrderProduct("콜라", 6);
            LocalDate orderDate = LocalDate.parse("2024-11-01");

            // when
            BuyResult buyResult = convenienceStore.buy(orderProduct, orderDate);

            // then
            assertThat(buyResult.getBuyType()).isSameAs(BuyType.PROMOTION);
            assertThat(buyResult.getBuyState()).isSameAs(BuyState.COMPLETE);
            assertThat(buyResult.getPromotionPriceQuantity()).isEqualTo(4);
            assertThat(buyResult.getBonusQuantity()).isEqualTo(2);
            assertThat(buyResult.getPendingQuantity()).isEqualTo(0);
            assertThat(buyResult.getRegularPriceQuantity()).isEqualTo(0);
        }

    }

    @Nested
    @DisplayName("프로모션 재고가 충분한 경우")
    class promotionFullStock {

        @Test
        void 부분_적용되었는지_확인할_수_있다() {
            // given
            List<Promotion> promotions = List.of(
                    new Promotion("2+1", 2, 1,
                            LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"))
            );
            List<Product> products = List.of(
                    new Product("콜라", 1000, 10, "2+1"),
                    new Product("콜라", 1000, 10, null)
            );
            ConvenienceStore convenienceStore = new ConvenienceStore(products, promotions);
            OrderProduct orderProduct = new OrderProduct("콜라", 7);
            LocalDate orderDate = LocalDate.parse("2024-11-01");

            // when
            BuyResult buyResult = convenienceStore.buy(orderProduct, orderDate);

            // then
            assertThat(buyResult.getBuyType()).isSameAs(BuyType.PROMOTION);
            assertThat(buyResult.getBuyState()).isSameAs(BuyState.PARTIALLY_PROMOTED);
            assertThat(buyResult.getPromotionPriceQuantity()).isEqualTo(4);
            assertThat(buyResult.getBonusQuantity()).isEqualTo(2);
            assertThat(buyResult.getPendingQuantity()).isEqualTo(1);
            assertThat(buyResult.getRegularPriceQuantity()).isEqualTo(0);
        }

        @Test
        void 증정_상품을_추가할_수_있는지_확인할_수_있다() {
            // given
            List<Promotion> promotions = List.of(
                    new Promotion("2+1", 2, 1,
                            LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"))
            );
            List<Product> products = List.of(
                    new Product("콜라", 1000, 10, "2+1"),
                    new Product("콜라", 1000, 10, null)
            );
            ConvenienceStore convenienceStore = new ConvenienceStore(products, promotions);
            OrderProduct orderProduct = new OrderProduct("콜라", 8);
            LocalDate orderDate = LocalDate.parse("2024-11-01");

            // when
            BuyResult buyResult = convenienceStore.buy(orderProduct, orderDate);

            // then
            assertThat(buyResult.getBuyType()).isSameAs(BuyType.PROMOTION);
            assertThat(buyResult.getBuyState()).isSameAs(BuyState.BONUS_ADDABLE);
            assertThat(buyResult.getPromotionPriceQuantity()).isEqualTo(4);
            assertThat(buyResult.getBonusQuantity()).isEqualTo(2);
            assertThat(buyResult.getPendingQuantity()).isEqualTo(2);
            assertThat(buyResult.getRegularPriceQuantity()).isEqualTo(0);
        }

        @Test
        void 전체_적용되었는지_확인할_수_있다() {
            // given
            List<Promotion> promotions = List.of(
                    new Promotion("2+1", 2, 1,
                            LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"))
            );
            List<Product> products = List.of(
                    new Product("콜라", 1000, 10, "2+1"),
                    new Product("콜라", 1000, 10, null)
            );
            ConvenienceStore convenienceStore = new ConvenienceStore(products, promotions);
            OrderProduct orderProduct = new OrderProduct("콜라", 9);
            LocalDate orderDate = LocalDate.parse("2024-11-01");

            // when
            BuyResult buyResult = convenienceStore.buy(orderProduct, orderDate);

            // then
            assertThat(buyResult.getBuyType()).isSameAs(BuyType.PROMOTION);
            assertThat(buyResult.getBuyState()).isSameAs(BuyState.COMPLETE);
            assertThat(buyResult.getPromotionPriceQuantity()).isEqualTo(6);
            assertThat(buyResult.getBonusQuantity()).isEqualTo(3);
            assertThat(buyResult.getPendingQuantity()).isEqualTo(0);
            assertThat(buyResult.getRegularPriceQuantity()).isEqualTo(0);
        }

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
