package com.github.supermarket.telegram.command.common;

import com.github.supermarket.telegram.handler.inline.InlineCommand;
import com.github.supermarket.telegram.handler.inline.InlineUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class UnknownInlineCommand implements InlineCommand {
    public static final String UNKNOWN_MESSAGE = "Неизвестная inline команда: %s";

    public UnknownInlineCommand() {
    }

    @Override
    public void execute(AbsSender sender, InlineQuery callbackQuery) throws Exception {
        String commandName = InlineUtils.commandNameFromQuery(callbackQuery.getQuery());
        String message = String.format(UNKNOWN_MESSAGE, commandName);
        sender.execute(
                SendMessage.builder()
                        .text(message)
                        .parseMode("html")
                        .chatId(callbackQuery.getFrom().getId().toString())
                        .build()
        );
    }

    @Override
    public String getInlineCommandName() {
        return "inline.unknown";
    }
}
