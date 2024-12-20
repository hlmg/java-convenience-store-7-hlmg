package store.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.exception.StoreException;
import store.model.order.BuyResult;
import store.model.order.OrderOrganizer;
import store.model.order.OrderProduct;
import store.model.product.Product;
import store.model.product.SellingProduct;
import store.model.product.SellingProductSnapshot;
import store.model.product.SellingProducts;
import store.model.promotion.Promotion;
import store.model.promotion.PromotionResult;
import store.model.promotion.Promotions;

public class ConvenienceStore {

    private final OrderOrganizer orderOrganizer = new OrderOrganizer();
    private final SellingProducts sellingProducts;
    private final Promotions promotions;

    public ConvenienceStore(List<Product> products, List<Promotion> promotions) {
        this.sellingProducts = new SellingProducts(products);
        this.promotions = new Promotions(promotions);
    }

    public List<SellingProductSnapshot> getSellingProductSnapshots() {
        return sellingProducts.getSellingProductSnapshots();
    }

    public List<BuyResult> buyProducts(List<OrderProduct> orderProducts, LocalDate orderDate) {
        orderProducts = orderOrganizer.organizeOrderProducts(orderProducts);
        List<BuyResult> buyResults = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            buyResults.add(buy(orderProduct, orderDate));
        }
        return buyResults;
    }

    public void deductProductsStock(List<BuyResult> buyResults) {
        for (BuyResult buyResult : buyResults) {
            SellingProduct sellingProduct = sellingProducts.getProductBy(buyResult.getProductName());
            if (buyResult.isPromotionOrder()) {
                sellingProduct.deductPromotionStockFirst(buyResult.getTotalBuyQuantity());
                return;
            }
            sellingProduct.deductRegularStockFirst(buyResult.getTotalBuyQuantity());
        }
    }

    private BuyResult buy(OrderProduct orderProduct, LocalDate orderDate) {
        SellingProduct sellingProduct = sellingProducts.getProductBy(orderProduct.name());
        checkEnoughStock(sellingProduct, orderProduct.quantity());
        Optional<Promotion> promotion = promotions.findActivePromotion(sellingProduct.getPromotion(), orderDate);
        if (promotion.isPresent()) {
            return proceedPromotionOrder(sellingProduct, orderProduct, promotion.get());
        }
        return proceedRegularOrder(sellingProduct, orderProduct);
    }

    private void checkEnoughStock(SellingProduct sellingProduct, int quantity) {
        if (sellingProduct.isStockLessThan(quantity)) {
            throw new StoreException("재고 수량을 초과하여 구매할 수 없습니다.");
        }
    }

    private BuyResult proceedPromotionOrder(SellingProduct sellingProduct, OrderProduct orderProduct,
                                            Promotion promotion) {
        int promotionStock = sellingProduct.getPromotionStock();
        PromotionResult promotionResult = promotion.apply(promotionStock, orderProduct.quantity());
        return BuyResult.createPromotionResult(sellingProduct.getSnapShot(), promotionResult);
    }

    private BuyResult proceedRegularOrder(SellingProduct sellingProduct, OrderProduct orderProduct) {
        return BuyResult.createRegularOrder(sellingProduct.getSnapShot(), orderProduct.quantity());
    }

}
