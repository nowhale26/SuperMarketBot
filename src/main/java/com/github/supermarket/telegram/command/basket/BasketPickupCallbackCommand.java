package com.github.supermarket.telegram.command.basket;

import com.github.supermarket.telegram.handler.callback.CallbackCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.github.supermarket.telegram.command.basket.BasketCheckOutCallbackCommand.PICKUP_COMMAND;

@Component
public class BasketPickupCallbackCommand implements CallbackCommand {
    @Override
    public void execute(AbsSender sender, CallbackQuery callbackQuery) throws Exception {
        Message message = callbackQuery.getMessage();
        sender.execute(SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("Ваш заказ передан на кухню. Он будет готов через 15 минут.")
                .parseMode("html")
                .build());

        sender.execute(ForwardMessage.builder()
                .fromChatId(message.getChatId().toString())
                .chatId("@canteensupermarketbot")
                .messageId(message.getMessageId())
                .build());
    }

    @Override
    public String getCallbackCommandName() {
        return PICKUP_COMMAND;
    }
}
