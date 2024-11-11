package store.io;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.exception.StoreException;
import store.model.OrderProduct;
import store.model.UserInputCommand;

public class InputView {

    public static final Pattern ORDER_PRODUCT_PATTERN = Pattern.compile("^\\[([\\w가-힣]+)-([1-9]\\d*)]$");

    public List<OrderProduct> getOrderProductsFromUser() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String userInput = Console.readLine();
        String[] splitOrderProducts = userInput.split(",");
        List<OrderProduct> orderProducts = Arrays.stream(splitOrderProducts)
                .map(this::toOrderProduct)
                .toList();
        System.out.println();
        return orderProducts;
    }

    public UserInputCommand askAddBonus(String productName) {
        String message = String.format("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)", productName);
        System.out.println(message);
        return getUserInputCommand();
    }

    public UserInputCommand askRegularPricePayment(String productName, int quantity) {
        String message = String.format("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", productName, quantity);
        System.out.println(message);
        return getUserInputCommand();
    }

    public UserInputCommand askMembershipDiscount() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return getUserInputCommand();
    }

    public UserInputCommand askAdditionalPurchase() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return getUserInputCommand();
    }

    private OrderProduct toOrderProduct(String orderProduct) {
        Matcher matcher = ORDER_PRODUCT_PATTERN.matcher(orderProduct);
        if (!matcher.matches()) {
            throw new StoreException("올바르지 않은 형식으로 입력했습니다.");
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

    private UserInputCommand getUserInputCommand() {
        String userInput = Console.readLine();
        UserInputCommand userInputCommand = UserInputCommand.from(userInput);
        System.out.println();
        return userInputCommand;
    }

}
