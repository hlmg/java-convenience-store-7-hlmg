package store.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.exception.StoreException;
import store.model.order.BuyResult;
import store.model.order.BuyType;
import store.model.order.OrderOrganizer;
import store.model.order.OrderProduct;
import store.model.product.Product;
import store.model.product.SellingProduct;
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

    public List<SellingProduct> getSellingProducts() {
        return sellingProducts.getProducts();
    }

    public List<BuyResult> order(List<OrderProduct> orderProducts, LocalDate orderDate) {
        orderProducts = orderOrganizer.organizeOrderProducts(orderProducts);
        List<BuyResult> buyResults = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            buyResults.add(buy(orderProduct, orderDate));
        }
        return buyResults;
    }

    private BuyResult buy(OrderProduct orderProduct, LocalDate orderDate) {
        SellingProduct sellingProduct = sellingProducts.getProductBy(orderProduct.name());
        int stock = sellingProduct.getStock();
        if (stock < orderProduct.quantity()) {
            throw new StoreException("재고 수량을 초과하여 구매할 수 없습니다.");
        }
        Optional<Promotion> promotion = promotions.findActivePromotion(sellingProduct.getPromotion(), orderDate);
        if (promotion.isPresent()) {
            return proceedPromotionOrder(sellingProduct, orderProduct, promotion.get());
        }
        return proceedRegularOrder(sellingProduct, orderProduct);
    }

    public void deductProductsStock(List<BuyResult> buyResults) {
        for (BuyResult buyResult : buyResults) {
            String productName = buyResult.productName();
            SellingProduct sellingProduct = sellingProducts.getProductBy(productName);
            sellingProduct.deductStock(buyResult.getTotalBuyQuantity(), BuyType.PROMOTION);
        }
    }

    private BuyResult proceedPromotionOrder(SellingProduct sellingProduct, OrderProduct orderProduct,
                                            Promotion promotion) {
        if (sellingProduct.ifPromotionStockLessThanOrEquals(orderProduct.quantity())) {
            return proceedLowStockPromotion(sellingProduct, orderProduct, promotion);
        }
        return proceedFullStockPromotion(sellingProduct, orderProduct, promotion);
    }

    private BuyResult proceedLowStockPromotion(SellingProduct sellingProduct, OrderProduct orderProduct,
                                               Promotion promotion) {
        int promotionStock = sellingProduct.getPromotionStock();
        PromotionResult promotionResult = promotion.apply(promotionStock);

        int pendingQuantity = (orderProduct.quantity() - promotionStock) + promotionResult.remain();
        int price = sellingProduct.getPrice();
        return BuyResult.createPartiallyPromotedResult(orderProduct.name(), price, promotionResult.buy(),
                promotionResult.get(),
                pendingQuantity);
    }

    private BuyResult proceedFullStockPromotion(SellingProduct sellingProduct, OrderProduct orderProduct,
                                                Promotion promotion) {
        PromotionResult promotionResult = promotion.apply(orderProduct.quantity());
        int pendingQuantity = promotionResult.remain();
        int price = sellingProduct.getPrice();
        if (promotion.isBonusApplicable(promotionResult.remain())) {
            return BuyResult.createBonusAddableResult(orderProduct.name(), price, promotionResult.buy(),
                    promotionResult.get(), pendingQuantity);
        }
        return BuyResult.createPartiallyPromotedResult(orderProduct.name(), price, promotionResult.buy(),
                promotionResult.get(), pendingQuantity);
    }

    private BuyResult proceedRegularOrder(SellingProduct sellingProduct, OrderProduct orderProduct) {
        int price = sellingProduct.getPrice();
        return BuyResult.createRegularCompleteResult(orderProduct.name(), price, orderProduct.quantity());
    }

}
