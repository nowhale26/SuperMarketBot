package com.github.supermarket.telegram.handler.callback;

public class CallbackData<T> {
    private final T value;
    private final String commandName;

    public CallbackData(String commandName, T value) {
        this.value = value;
        this.commandName = commandName;
    }

    public T getValue() {
        return value;
    }

    public String getCommandName() {
        return commandName;
    }
}
