package com.github.supermarket.telegram.command.common.arch;

import com.github.supermarket.telegram.handler.message.MessageCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Unknown {@link MessageCommand}.
 */
// отключено!!!!  @Component
public class UnknownMessageCommand implements MessageCommand {
    public static final String UNKNOWN_MESSAGE = "Неизвестная команда. Чтобы посмотреть список команд напишите: Помощь";

    public UnknownMessageCommand() {
    }

    @Override
    public String getMessageCommandName() {
        return "/unknown";
    }

    @Override
    public void execute(AbsSender sender, Message message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.enableHtml(true);
        sendMessage.setText(UNKNOWN_MESSAGE);

        sender.execute(sendMessage);
    }
}