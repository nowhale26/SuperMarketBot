package com.github.supermarket.telegram.command.basket;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.storecart.StoreCartService;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.github.supermarket.service.storecart.model.StoreCartFilter;
import com.github.supermarket.telegram.handler.message.MessageCommand;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Stop {@link MessageCommand}.
 */
@Component
public class BasketMessageCommand implements MessageCommand {
    private final BasketSender basketSender;

    public BasketMessageCommand(BasketSender basketSender) {
        this.basketSender = basketSender;
    }

    @Override
    public String getMessageCommandName() {
        return "/basket";
    }

    @Override
    public void execute(AbsSender sender, Message message) throws Exception {
        basketSender.sendMessages(sender, message);
    }
}