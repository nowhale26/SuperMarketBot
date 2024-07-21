package com.github.supermarket.telegram;

import com.github.supermarket.exception.ChatBotException;
import com.github.supermarket.telegram.handler.UpdateHandler;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class SupermarketBot extends TelegramLongPollingBot {
    private final String username;
    private final String token;
    private final Long storeId;
    private final List<UpdateHandler> updateHandlers;

    public SupermarketBot(String username, String token, Long storeId, List<UpdateHandler> updateHandlers) {
        this.username = username;
        this.token = token;
        this.storeId = storeId;
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
        SessionContext.setSession(new UserSession(storeId));
        try {
            for (UpdateHandler updateHandler : updateHandlers) {
                if (updateHandler.handleUpdate(this, update)) {
                    return;
                }
            }
        } catch (Exception e) {
            throw new ChatBotException(e);
        } finally {
            SessionContext.removeUserSession();
        }
    }

    public void afterRegisterBot() {

    }
}
