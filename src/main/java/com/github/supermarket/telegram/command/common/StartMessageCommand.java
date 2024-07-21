package com.github.supermarket.telegram.command.common;

import com.github.supermarket.service.store.StoreService;
import com.github.supermarket.service.store.model.Store;
import com.github.supermarket.telegram.handler.message.MessageCommand;
import com.github.supermarket.telegram.session.SessionContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Start {@link MessageCommand}.
 */
@Component
public class StartMessageCommand implements MessageCommand {
    public static final String START_MESSAGE = """
            Вы подключились к магазину: <b>%s</b>
            
            Для выбора и покупки товаров воспользуйтесь:
                1) на мобильном устройстве кнопкой <b>"Меню"</b>, расположенной слева от поля ввода
                2) на компьютере кнопкой <b>"/"</b>, расположенной справа от поля ввода  
                                
            Также для получения списка команд вы можете ввести /help 
            """;

    private final StoreService storeService;

    public StartMessageCommand(StoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    public void execute(AbsSender sender, Message message) throws Exception {
        Store store = storeService.get(SessionContext.getUserSession().getStoreId());

        sender.execute(SendMessage.builder()
                .chatId(message.getChatId().toString())
                .parseMode("html")
                .text(String.format(START_MESSAGE, store.getName()))
                .build()
        );
    }

    @Override
    public String getMessageCommandName() {
        return "/start";
    }
}