package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.exception.StoreException;
import store.model.order.BuyResult;
import store.model.order.BuyState;
import store.model.order.BuyType;
import store.model.order.OrderProduct;
import store.model.product.Product;
import store.model.promotion.Promotion;

@SuppressWarnings("NonAsciiCharacters")
class ConvenienceStoreTest {

    @Test
    void 없는_상품을_구매하면_예외가_발생한다() {
        // given
        List<Product> products = List.of(new Product("콜라", 1000, 10, null));
        ConvenienceStore convenienceStore = new ConvenienceStore(products, List.of());

        List<OrderProduct> orderProduct = List.of(new OrderProduct("에너지바", 10));
        LocalDate orderDate = LocalDate.parse("2024-11-01");

        // when & then
        assertThatThrownBy(() -> convenienceStore.order(orderProduct, orderDate))
                .isInstanceOf(StoreException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    void 재고보다_많은_수량을_구매하면_예외가_발생한다() {
        // given
        List<Product> products = List.of(new Product("콜라", 1000, 10, null));
        ConvenienceStore convenienceStore = new ConvenienceStore(products, List.of());

        List<OrderProduct> orderProduct = List.of(new OrderProduct("콜라", 11));
        LocalDate orderDate = LocalDate.parse("2024-11-01");

        // when & then
        assertThatThrownBy(() -> convenienceStore.order(orderProduct, orderDate))
                .isInstanceOf(StoreException.class)
                .hasMessage("재고 수량을 초과하여 구매할 수 없습니다.");
    }

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
            List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 8));
            LocalDate orderDate = LocalDate.parse("2024-11-01");

            // when
            List<BuyResult> buyResults = convenienceStore.order(orderProducts, orderDate);

            // then
            assertThat(buyResults).hasSize(1)
                    .extracting("buyType", "buyState", "promotionPriceQuantity", "bonusQuantity", "pendingQuantity",
                            "regularPriceQuantity")
                    .containsExactly(tuple(BuyType.PROMOTION, BuyState.PARTIALLY_PROMOTED, 4, 2, 2, 0));
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
            List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 6));
            LocalDate orderDate = LocalDate.parse("2024-11-01");

            // when
            List<BuyResult> buyResults = convenienceStore.order(orderProducts, orderDate);

            // then
            assertThat(buyResults).hasSize(1)
                    .extracting("buyType", "buyState", "promotionPriceQuantity", "bonusQuantity", "pendingQuantity",
                            "regularPriceQuantity")
                    .containsExactly(tuple(BuyType.PROMOTION, BuyState.COMPLETE, 4, 2, 0, 0));
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
            List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 7));
            LocalDate orderDate = LocalDate.parse("2024-11-01");

            // when
            List<BuyResult> buyResults = convenienceStore.order(orderProducts, orderDate);

            // then
            assertThat(buyResults).hasSize(1)
                    .extracting("buyType", "buyState", "promotionPriceQuantity", "bonusQuantity", "pendingQuantity",
                            "regularPriceQuantity")
                    .containsExactly(tuple(BuyType.PROMOTION, BuyState.PARTIALLY_PROMOTED, 4, 2, 1, 0));
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
            List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 8));
            LocalDate orderDate = LocalDate.parse("2024-11-01");

            // when
            List<BuyResult> buyResults = convenienceStore.order(orderProducts, orderDate);

            // then
            assertThat(buyResults).hasSize(1)
                    .extracting("buyType", "buyState", "promotionPriceQuantity", "bonusQuantity", "pendingQuantity",
                            "regularPriceQuantity")
                    .containsExactly(tuple(BuyType.PROMOTION, BuyState.BONUS_ADDABLE, 4, 2, 2, 0));
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
            List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 9));
            LocalDate orderDate = LocalDate.parse("2024-11-01");

            // when
            List<BuyResult> buyResults = convenienceStore.order(orderProducts, orderDate);

            // then
            assertThat(buyResults).hasSize(1)
                    .extracting("buyType", "buyState", "promotionPriceQuantity", "bonusQuantity", "pendingQuantity",
                            "regularPriceQuantity")
                    .containsExactly(tuple(BuyType.PROMOTION, BuyState.COMPLETE, 6, 3, 0, 0));
        }

    }

    @Test
    void 프로모션이_없는_상품은_일반_주문으로_처리된다() {
        // given
        List<Promotion> promotions = List.of(
                new Promotion("2+1", 2, 1,
                        LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"))
        );
        List<Product> products = List.of(
                new Product("콜라", 1000, 10, null)
        );
        ConvenienceStore convenienceStore = new ConvenienceStore(products, promotions);
        List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 9));
        LocalDate orderDate = LocalDate.parse("2024-11-01");

        // when
        List<BuyResult> buyResults = convenienceStore.order(orderProducts, orderDate);

        // then
        assertThat(buyResults).hasSize(1)
                .extracting("buyType", "buyState", "promotionPriceQuantity", "bonusQuantity", "pendingQuantity",
                        "regularPriceQuantity")
                .containsExactly(tuple(BuyType.REGULAR, BuyState.COMPLETE, 0, 0, 0, 9));
    }

    @Test
    void 프로모션_기간이_아닌_상품은_일반_주문으로_처리된다() {
        // given
        List<Promotion> promotions = List.of(
                new Promotion("2+1", 2, 1,
                        LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-30"))
        );
        List<Product> products = List.of(
                new Product("콜라", 1000, 5, "2+1"),
                new Product("콜라", 1000, 5, null)
        );
        ConvenienceStore convenienceStore = new ConvenienceStore(products, promotions);
        List<OrderProduct> orderProducts = List.of(new OrderProduct("콜라", 9));
        LocalDate orderDate = LocalDate.parse("2024-12-01");

        // when
        List<BuyResult> buyResults = convenienceStore.order(orderProducts, orderDate);

        // then
        assertThat(buyResults).hasSize(1)
                .extracting("buyType", "buyState", "promotionPriceQuantity", "bonusQuantity", "pendingQuantity",
                        "regularPriceQuantity")
                .containsExactly(tuple(BuyType.REGULAR, BuyState.COMPLETE, 0, 0, 0, 9));
    }

}
