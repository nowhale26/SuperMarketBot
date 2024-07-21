package com.github.supermarket.telegram.handler.callback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.supermarket.exception.ChatBotException;
import org.springframework.stereotype.Component;

@Component
public class CallbackUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toData(String callbackCommandName, Object value) {
        try {
            return callbackCommandName + ":" + OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new ChatBotException(e);
        }
    }

    public static <T> CallbackData<T> fromData(String callbackData, Class<T> valueClass) {
        int pos = callbackData.indexOf(":");

        String commandName = pos == -1 ? callbackData : callbackData.substring(0, pos);
        String value = pos == -1 ? null : callbackData.substring(pos + 1);
        return new CallbackData(commandName, value != null ? OBJECT_MAPPER.convertValue(value, valueClass) : null);
    }

    public static String commandNameFomData(String callbackData) {
        int pos = callbackData.indexOf(":");
        return pos == -1 ? callbackData : callbackData.substring(0, pos);
    }

    public static <T> T valueFromData(String callbackData, Class<T> valueClass) {
        int pos = callbackData.indexOf(":");
        String value = pos == -1 ? null : callbackData.substring(pos + 1);
        return value != null ? OBJECT_MAPPER.convertValue(value, valueClass) : null;
    }
}
