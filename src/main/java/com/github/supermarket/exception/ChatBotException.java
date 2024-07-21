package com.github.supermarket.exception;

public class ChatBotException extends RuntimeException {
    public ChatBotException() {
        super();
    }

    public ChatBotException(String message) {
        super(message);
    }

    public ChatBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatBotException(Throwable cause) {
        super(cause);
    }

    protected ChatBotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
