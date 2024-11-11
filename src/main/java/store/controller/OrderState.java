package store.controller;

import store.model.user.UserInputCommand;

public enum OrderState {

    IN_PROGRESS,
    COMPLETE,
    ;

    public boolean inProgress() {
        return this == IN_PROGRESS;
    }

    public OrderState additionalOrder(UserInputCommand additionalOrder) {
        if (additionalOrder == UserInputCommand.NO) {
            return COMPLETE;
        }
        return IN_PROGRESS;
    }

}
