package com.github.supermarket.telegram.handler.inline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.supermarket.exception.ChatBotException;
import com.github.supermarket.telegram.handler.callback.CallbackCommand;
import org.springframework.stereotype.Component;

@Component
public class InlineUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toQuery(CallbackCommand command, Object value) {
        try {
            return command.getCallbackCommandName() + ":" + OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new ChatBotException(e);
        }
    }

//    public static <T> CallbackData<T> fromData(String callbackData, Class<T> valueClass) {
//        int pos = callbackData.indexOf(":");
//
//        String commandName = pos == -1 ? callbackData : callbackData.substring(0, pos);
//        String value = pos == -1 ? null : callbackData.substring(pos + 1);
//
//        return new CallbackData(commandName, value != null ? OBJECT_MAPPER.convertValue(value, valueClass) : null);
//    }

    public static String commandNameFromQuery(String query) {
        int pos = query.indexOf(":");

        return pos == -1 ? query : query.substring(0, pos);
    }

    public static <T> T valueFromQuery(String query, Class<T> valueClass) {
        int pos = query.indexOf(":");

        String value = pos == -1 ? null : query.substring(pos + 1);

        return value != null ? OBJECT_MAPPER.convertValue(value, valueClass) : null;
    }

}
