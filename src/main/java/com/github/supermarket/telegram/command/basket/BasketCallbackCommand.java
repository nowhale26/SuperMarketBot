package com.github.supermarket.telegram.command.basket;

import com.github.supermarket.telegram.handler.callback.CallbackCommand;
import com.github.supermarket.telegram.handler.message.MessageCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Stop {@link MessageCommand}.
 */
@Component
public class BasketCallbackCommand implements CallbackCommand {
    public static final String BASKET_COMMAND = "/basket";

    private final BasketSender basketSender;

    public BasketCallbackCommand(BasketSender basketSender) {
        this.basketSender = basketSender;
    }

    @Override
    public void execute(AbsSender sender, CallbackQuery callbackQuery) throws Exception {
        Message message = callbackQuery.getMessage();
        basketSender.sendMessages(sender, message);
    }

    @Override
    public String getCallbackCommandName() {
        return BASKET_COMMAND;
    }
}