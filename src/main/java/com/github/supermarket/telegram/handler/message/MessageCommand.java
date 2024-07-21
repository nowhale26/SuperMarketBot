package com.github.supermarket.telegram.handler.message;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Command interface for handling telegram-bot commands.
 */
public interface MessageCommand {

    /**
     * Main method, which is executing command logic.
     *
     * @param absSender
     * @param message provided {@link Update} object with all the needed data for command.
     */
    void execute(AbsSender absSender, Message message) throws Exception;

    /**
     * Возвращает имя message команды
     *
     * @return имя message команды
     */
    String getMessageCommandName();
}