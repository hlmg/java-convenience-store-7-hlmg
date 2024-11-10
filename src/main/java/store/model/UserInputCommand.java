package store.model;

public enum UserInputCommand {

    YES("Y"),
    NO("N"),
    ;

    private final String symbol;

    UserInputCommand(String symbol) {
        this.symbol = symbol;
    }

}
