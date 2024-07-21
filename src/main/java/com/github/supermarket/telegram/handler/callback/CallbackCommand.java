package com.github.supermarket.telegram.handler.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface CallbackCommand {


    /**
     * Main method, which is executing command logic.
     *
     * @param sender
     * @param callbackQuery provided {@link Update} object with all the needed data for command.
     */
    void execute(AbsSender sender, CallbackQuery callbackQuery) throws Exception;

    /**
     * Возвращает имя callback команды
     *
     * @return имя callback команды
     */
    String getCallbackCommandName();

}
