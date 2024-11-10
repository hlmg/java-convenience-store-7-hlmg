package store.io;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.model.OrderProduct;

public class InputView {

    public static final Pattern ORDER_PRODUCT_PATTERN = Pattern.compile("^\\[([\\w가-힣]+)-([1-9]\\d*)]$");

    public List<OrderProduct> getOrderProductsFromUser() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String userInput = Console.readLine();
        String[] splitOrderProducts = userInput.split(",");
        return Arrays.stream(splitOrderProducts)
                .map(this::toOrderProduct)
                .toList();
    }

    private OrderProduct toOrderProduct(String orderProduct) {
        Matcher matcher = ORDER_PRODUCT_PATTERN.matcher(orderProduct);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        return new OrderProduct(matcher.group(1), toInt(matcher.group(2)));
    }

    private int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("숫자로 변환할 수 없습니다.");
        }
    }

}
