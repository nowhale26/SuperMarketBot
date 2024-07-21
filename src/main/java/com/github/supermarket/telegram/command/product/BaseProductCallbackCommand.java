package com.github.supermarket.telegram.command.product;

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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
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
public abstract class BaseProductCallbackCommand implements CallbackCommand {
    public static final String ADD_PRODUCT_COMMAND = "/product.add";
    public static final String REMOVE_PRODUCT_COMMAND = "/product.remove";

    private final StoreCartService storeCartService;
    private final CustomerService customerService;
    private final ProductBuilder productBuilder;

    public BaseProductCallbackCommand(StoreCartService storeCartService, CustomerService customerService, ProductBuilder productBuilder) {
        this.storeCartService = storeCartService;
        this.customerService = customerService;
        this.productBuilder = productBuilder;
    }

    @Override
    public void execute(AbsSender sender, CallbackQuery callbackQuery) throws Exception {
        Message message = callbackQuery.getMessage();
        Customer customer = getCustomer(message);
        StoreCartDelta deltaStoreCart = buildStoreCartDelta(callbackQuery, customer);
        StoreCart modifyStoreCart = modifyStoreCart(deltaStoreCart);

        sender.execute(EditMessageReplyMarkup.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .replyMarkup(productBuilder.buildButtons(modifyStoreCart.getProductDetail().getId(), modifyStoreCart.getProductDetail().getPrice(), modifyStoreCart.getQuantity()))
                .build()
        );
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

//    protected List<List<InlineKeyboardButton>> createButtons(StoreCart storeCart) {
//        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
//        buttons.add(
//                Optional.ofNullable(storeCart.getQuantity())
//                        .filter(quantity -> ObjectUtils.compare(quantity, BigDecimal.ZERO) > 0).map(quantity -> List.of(
//                                builder().text("+").callbackData(CallbackUtils.toData(ADD_PRODUCT_COMMAND, storeCart.getProductDetail().getId())).build(),
//                                builder().text(NumberFormat.getInstance().format(quantity)).callbackData(CallbackUtils.toData(EMPTY_CALLBACK_COMMAND, storeCart.getProductDetail().getId())).build(),
//                                builder().text("-").callbackData(CallbackUtils.toData(REMOVE_PRODUCT_COMMAND, storeCart.getProductDetail().getId())).build())
//                        ).orElseGet(() -> List.of(
//                                builder().text(NumberFormat.getInstance().format(storeCart.getProductDetail().getPrice()) + " руб.").callbackData(CallbackUtils.toData(ADD_PRODUCT_COMMAND, storeCart.getProductDetail().getId())).build())
//                        )
//        );
//        return buttons;
//    }

    protected abstract StoreCart modifyStoreCart(StoreCartDelta storeCartDelta);

}
