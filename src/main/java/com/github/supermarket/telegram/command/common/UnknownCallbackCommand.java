package com.github.supermarket.telegram.command.common;

import com.github.supermarket.telegram.handler.callback.CallbackCommand;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class UnknownCallbackCommand implements CallbackCommand {
    public static final String UNKNOWN_MESSAGE = "Неизвестная callback команда: %s";

    public UnknownCallbackCommand() {
    }

    @Override
    public void execute(AbsSender sender, CallbackQuery callbackQuery) throws Exception {
        String commandName = CallbackUtils.commandNameFomData(callbackQuery.getData());
        String message = String.format(UNKNOWN_MESSAGE, commandName);
        sender.execute(
                SendMessage.builder()
                        .text(message)
                        .parseMode("html")
                        .chatId(callbackQuery.getMessage().getChatId().toString())
                        .build()
        );
    }

    @Override
    public String getCallbackCommandName() {
        return "callback.unknown";
    }
}
