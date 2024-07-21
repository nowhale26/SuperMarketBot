package com.github.supermarket.telegram.command.basket;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.storecart.StoreCartService;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.github.supermarket.service.storecart.model.StoreCartFilter;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.github.supermarket.telegram.command.basket.BaseBasketCallbackCommand.PICKUP_COMMAND;
import static com.github.supermarket.telegram.command.basket.BaseBasketCallbackCommand.PAY_COMMAND;

import java.util.ArrayList;
import java.util.List;

import static com.github.supermarket.telegram.command.basket.BasketCheckOutCallbackCommand.PROCEED_TO_PAYMENT_COMMAND;
import static com.github.supermarket.telegram.command.product.BaseProductCallbackCommand.ADD_PRODUCT_COMMAND;
import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.builder;

@Component
public class BasketSender {
    public static final String EMPTY_BASKET = "Корзина пуста";
    public static final String BASKET = "<b>\uD83D\uDED2 Корзина:</b>";

    private final StoreCartService storeCartService;
    private final BasketBuilder basketBuilder;

    public BasketSender(StoreCartService storeCartService, BasketBuilder basketBuilder) {
        this.storeCartService = storeCartService;
        this.basketBuilder = basketBuilder;
    }

    public void sendMessages(AbsSender sender, Message message) throws Exception {
        UserSession userSession = SessionContext.getUserSession();
        StoreCartFilter storeCartFilter = StoreCartFilter.builder()
                .storeId(userSession.getStoreId())
                .externalType(userSession.getExternalType())
                .externalId(userSession.getExternalId())
                .build();

        ListResponse<StoreCart> storeCartsResponse = storeCartService.getByParams(storeCartFilter);

        if (storeCartsResponse.getItems().isEmpty()) {
            sender.execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .parseMode("html")
                    .text(EMPTY_BASKET)
                    .build()
            );
        } else {
            sender.execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .parseMode("html")
                    .text(BASKET)
                    .build()
            );
            for (StoreCart storeCart : storeCartsResponse.getItems()) {
                sender.execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text(basketBuilder.buildText(storeCart))
                        .parseMode("html")
                        .replyMarkup(basketBuilder.buildButtons(storeCart))
                        .build()
                );
            }
            List<List<InlineKeyboardButton>> rows = new ArrayList<List<InlineKeyboardButton>>();
            rows.add(List.of(
                    builder().text("Оформить заказ").callbackData(CallbackUtils.toData(PROCEED_TO_PAYMENT_COMMAND,1)).build()));

            sender.execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("<b>Оформление заказа</b>")
                    .parseMode("html")
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(rows).build())
                    .build());
        }
    }

}
