package com.github.supermarket.telegram.command.common;

import com.github.supermarket.telegram.handler.callback.CallbackCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class EmptyCallbackCommand implements CallbackCommand {
    public static final String EMPTY_CALLBACK_COMMAND = "callback.empty";

    public EmptyCallbackCommand() {
    }

    @Override
    public void execute(AbsSender sender, CallbackQuery callbackQuery) throws Exception {
    }

    @Override
    public String getCallbackCommandName() {
        return EMPTY_CALLBACK_COMMAND;
    }
}
