package com.github.supermarket.telegram.command.basket;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.storecart.StoreCartService;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.github.supermarket.service.storecart.model.StoreCartFilter;
import com.github.supermarket.telegram.handler.callback.CallbackCommand;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.builder;

@Component
public class BasketCheckOutCallbackCommand implements CallbackCommand {
    public static final String PROCEED_TO_PAYMENT_COMMAND ="/basket.payment_bill";
    public static final String PICKUP_COMMAND ="/basket.pickup";
    public static final String PAYMENT_COMMAND ="/basket.payment";
    private final StoreCartService storeCartService;

    public BasketCheckOutCallbackCommand(StoreCartService storeCartService) {
        this.storeCartService = storeCartService;
    }

    @Override
    public void execute(AbsSender sender, CallbackQuery callbackQuery) throws Exception {
        UserSession userSession = SessionContext.getUserSession();
        StoreCartFilter storeCartFilter = StoreCartFilter.builder()
                .storeId(userSession.getStoreId())
                .externalType(userSession.getExternalType())
                .externalId(userSession.getExternalId())
                .build();

        ListResponse<StoreCart> storeCartsResponse = storeCartService.getByParams(storeCartFilter);
        StringBuilder checkoutText=new StringBuilder();
        BigDecimal sum=BigDecimal.ZERO;
        DecimalFormat decimalFormat = new DecimalFormat("#0.##");
        for(StoreCart storeCart : storeCartsResponse.getItems()){
            BigDecimal amount=storeCart.getProductDetail().getPrice().multiply(storeCart.getQuantity());
            checkoutText
                    .append(storeCart.getProductDetail().getProduct().getName())
                    .append(" ")
                    .append(decimalFormat.format(storeCart.getProductDetail().getPrice()))
                    .append(" руб. * ")
                    .append(decimalFormat.format(storeCart.getQuantity()))
                    .append(" = ")
                    .append(decimalFormat.format(amount))
                    .append(" руб.")
                    .append("\n")
                    .append("\n");
            sum=sum.add(amount);
        }
        checkoutText.append("Итого: ").append(decimalFormat.format(sum)).append(" руб.");
        String checkoutTextString = checkoutText.toString();

        List<List<InlineKeyboardButton>> rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(List.of(
                builder().text("Самовывоз").callbackData(CallbackUtils.toData(PICKUP_COMMAND,1)).build(),
                builder().text("Оплата картой").callbackData(CallbackUtils.toData(PAYMENT_COMMAND,1)).build()));

        Message message = callbackQuery.getMessage();
        Message message1 = sender.execute(SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(checkoutTextString)
                .parseMode("html")
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(rows).build())
                .build());

    }

    @Override
    public String getCallbackCommandName() {
        return PROCEED_TO_PAYMENT_COMMAND;
    }
}
