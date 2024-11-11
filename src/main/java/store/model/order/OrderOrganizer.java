package store.model.order;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderOrganizer {

    public List<OrderProduct> organizeOrderProducts(List<OrderProduct> orderProducts) {
        Map<String, Integer> productNameToQuantity = groupByProductName(orderProducts);
        return toOrderProducts(productNameToQuantity);
    }

    private Map<String, Integer> groupByProductName(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .collect(Collectors.groupingBy(
                        OrderProduct::name,
                        LinkedHashMap::new,
                        Collectors.summingInt(OrderProduct::quantity)
                ));
    }

    private List<OrderProduct> toOrderProducts(Map<String, Integer> productNameToQuantity) {
        return productNameToQuantity.entrySet().stream()
                .map(entry -> new OrderProduct(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

}
