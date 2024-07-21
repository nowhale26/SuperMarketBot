package com.github.supermarket.telegram;

import com.github.supermarket.exception.ChatBotException;
import com.github.supermarket.telegram.handler.UpdateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

//@Component
public class SupermarketBotOld extends TelegramLongPollingBot {
    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    private List<UpdateHandler> updateHandlers;

    public SupermarketBotOld(@Lazy List<UpdateHandler> updateHandlers) {
        this.updateHandlers = updateHandlers;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            for (UpdateHandler updateHandler : updateHandlers) {
                if (updateHandler.handleUpdate(this, update)) {
                    return;
                }
            }
        } catch (Exception e) {
            throw new ChatBotException(e);
        }
    }
}
