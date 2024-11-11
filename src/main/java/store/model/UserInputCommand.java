package store.model;

import java.util.Arrays;
import store.exception.StoreException;

public enum UserInputCommand {

    YES("Y"),
    NO("N"),
    ;

    private final String symbol;

    UserInputCommand(String symbol) {
        this.symbol = symbol;
    }

    public static UserInputCommand from(String symbol) {
        return Arrays.stream(UserInputCommand.values())
                .filter(command -> command.symbol.equals(symbol))
                .findAny()
                .orElseThrow(() -> new StoreException("잘못된 입력입니다."));
    }

}
