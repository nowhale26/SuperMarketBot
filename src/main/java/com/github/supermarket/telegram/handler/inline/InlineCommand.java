package com.github.supermarket.telegram.handler.inline;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface InlineCommand {

    /**
     * Main method, which is executing command logic.
     *
     * @param sender
     * @param inlineQuery provided {@link Update} object with all the needed data for command.
     */
    void execute(AbsSender sender, InlineQuery inlineQuery) throws Exception;

    /**
     * Возвращает имя inlineQuery команды
     *
     * @return имя inlineQuery команды
     */
    String getInlineCommandName();

}
