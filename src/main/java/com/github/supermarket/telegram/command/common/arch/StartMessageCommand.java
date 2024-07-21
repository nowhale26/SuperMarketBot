package com.github.supermarket.telegram.command.common.arch;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.catalog.CatalogService;
import com.github.supermarket.service.catalog.model.Catalog;
import com.github.supermarket.service.catalog.model.CatalogFilter;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import com.github.supermarket.telegram.handler.message.MessageCommand;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

import static com.github.supermarket.telegram.command.product.arch.ProductCallbackCommand.PRODUCT_CALLBACK_COMMAND;

/**
 * Start {@link MessageCommand}.
 */
// отключено!!!!  @Component
public class StartMessageCommand implements MessageCommand {
    private final CatalogService catalogService;

    public StartMessageCommand(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Override
    public void execute(AbsSender sender, Message message) throws Exception {
        sender.execute(SendMessage.builder()
                .chatId(message.getChatId().toString())
                .parseMode("html")
                .replyMarkup(createKeyboard())
                .text("Выберите нужный каталог:")
                .build()
        );
    }

    @Override
    public String getMessageCommandName() {
        return "/start";
    }

    private InlineKeyboardMarkup createKeyboard() {
        UserSession userSession = SessionContext.getUserSession();
        CatalogFilter catalogFilter = CatalogFilter.builder().storeId(userSession.getStoreId()).build();
        ListResponse<Catalog> catalogListResponse = catalogService.getByParams(catalogFilter);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (Catalog catalog : catalogListResponse.getItems()) {
            buttons.add(List.of(InlineKeyboardButton.builder()
                    .text(catalog.getName())
                    .callbackData(CallbackUtils.toData(PRODUCT_CALLBACK_COMMAND, catalog.getId()))
                    .build())
            );
        }
        return InlineKeyboardMarkup.builder().keyboard(buttons).build();
    }

    private ReplyKeyboardMarkup createKeyboard2() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(new ArrayList<>() {{
            add(new KeyboardRow() {{
                add("Каталог");
                add("Корзина");
                add("Кабинет");
                add("О сервисе");
            }});
        }});
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setSelective(true);
//        keyboardMarkup.setInputFieldPlaceholder("input_field_placeholder");
        return keyboardMarkup;
    }

}