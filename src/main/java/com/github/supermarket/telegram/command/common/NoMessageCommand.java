package com.github.supermarket.telegram.command.common;

import com.github.supermarket.telegram.handler.message.MessageCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * No {@link MessageCommand}.
 */
@Component
public class NoMessageCommand implements MessageCommand {
    public static final String NO_MESSAGE = "Поддерживаются команды начинающиеся со слеша (<b>/</b>)\n";
//            + "Чтобы посмотреть список команд введите /help";

    public static final String BASE_MESSAGE = """
                        
            Для выбора и покупки товаров воспользуйтесь кнопкой <b>"Меню"</b> (на мобильном устройстве) или кнопкой <b>"/"</b> (на компьютере)
                            
            Также для получения списка команд вы можете ввести /help 
            """;

    @Override
    public void execute(AbsSender sender, Message message) throws Exception {
        sender.execute(SendMessage.builder()
                .chatId(message.getChatId().toString())
                .parseMode("html")
                .text(NO_MESSAGE + BASE_MESSAGE)
                .build()
        );
    }

    @Override
    public String getMessageCommandName() {
        return "/nocommand";
    }
}