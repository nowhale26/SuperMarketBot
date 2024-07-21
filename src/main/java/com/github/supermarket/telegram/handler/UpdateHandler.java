package com.github.supermarket.telegram.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface UpdateHandler {

    boolean handleUpdate(AbsSender absSender, Update update) throws Exception;
}
