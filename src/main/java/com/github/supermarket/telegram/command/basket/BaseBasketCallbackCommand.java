package com.github.supermarket.telegram.command.basket;

import com.github.supermarket.service.customer.CustomerService;
import com.github.supermarket.service.customer.model.Customer;
import com.github.supermarket.service.storecart.StoreCartService;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.github.supermarket.service.storecart.model.StoreCartDelta;
import com.github.supermarket.telegram.handler.callback.CallbackCommand;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.supermarket.telegram.command.common.EmptyCallbackCommand.EMPTY_CALLBACK_COMMAND;
import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.builder;

@Component
public abstract class BaseBasketCallbackCommand implements CallbackCommand {
    public static final String ADD_BASKET_COMMAND = "/basket.add";
    public static final String REMOVE_BASKET_COMMAND = "/basket.remove";
    public static final String PICKUP_COMMAND = "/basket.pickup";
    public static final String PAY_COMMAND ="/basket.pay";


    private final StoreCartService storeCartService;
    private final CustomerService customerService;
    private final BasketBuilder basketBuilder;

    public BaseBasketCallbackCommand(StoreCartService storeCartService, CustomerService customerService, BasketBuilder basketBuilder) {
        this.storeCartService = storeCartService;
        this.customerService = customerService;
        this.basketBuilder = basketBuilder;
    }

    @Override
    public void execute(AbsSender sender, CallbackQuery callbackQuery) throws Exception {
        Message message = callbackQuery.getMessage();
        Customer customer = getCustomer(message);
        StoreCartDelta deltaStoreCart = buildStoreCartDelta(callbackQuery, customer);
        StoreCart modifyStoreCart = modifyStoreCart(deltaStoreCart);

        if (ObjectUtils.compare(modifyStoreCart.getQuantity(), BigDecimal.ZERO) > 0) {
            sender.execute(EditMessageText.builder()
                    .chatId(message.getChatId().toString())
                    .messageId(message.getMessageId())
                    .text(basketBuilder.buildText(modifyStoreCart))
                    .replyMarkup(basketBuilder.buildButtons(modifyStoreCart))
                    .parseMode("html")
                    .build()
            );
        } else {
            sender.execute(DeleteMessage.builder()
                    .chatId(message.getChatId().toString())
                    .messageId(message.getMessageId())
                    .build()
            );
        }
    }

    private StoreCartDelta buildStoreCartDelta(CallbackQuery callbackQuery, Customer customer) {
        UserSession userSession = SessionContext.getUserSession();
        Long productDetailId = CallbackUtils.valueFromData(callbackQuery.getData(), Long.class);
        return new StoreCartDelta(userSession.getStoreId(), customer.getId(), productDetailId, BigDecimal.ONE);
    }

    private Customer getCustomer(Message message) {
        UserSession userSession = SessionContext.getUserSession();
        Customer customer = new Customer(null, userSession.getStoreId(), userSession.getExternalType(), userSession.getExternalId(),
                message.getFrom().getUserName(), message.getFrom().getLastName() + " " + message.getFrom().getLastName(), null, null, null);
        return customerService.createOrUpdate(customer);
    }

    protected abstract StoreCart modifyStoreCart(StoreCartDelta storeCartDelta);

}
