package com.github.supermarket.telegram.handler;

import com.github.supermarket.enums.ExternalType;
import com.github.supermarket.telegram.command.common.UnknownCallbackCommand;
import com.github.supermarket.telegram.handler.callback.CallbackCommand;
import com.github.supermarket.telegram.handler.message.MessageCommand;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
public class CallbackQueryUpdateHandler implements UpdateHandler {
    private static Logger logger = LoggerFactory.getLogger(CallbackQueryUpdateHandler.class);
    private final ImmutableMap<String, CallbackCommand> commands;
    private final UnknownCallbackCommand unknownCommand;

    public CallbackQueryUpdateHandler(List<CallbackCommand> callbackCommands, UnknownCallbackCommand unknownCommand) {
        ImmutableMap.Builder builder = ImmutableMap.<String, MessageCommand>builder();
        for (CallbackCommand command : callbackCommands) {
            validate(command);
            builder.put(command.getCallbackCommandName(), command);
        }
        commands = builder.build();
        this.unknownCommand = unknownCommand;
    }

    private void validate(CallbackCommand command) {
        if (StringUtils.isBlank(command.getCallbackCommandName())) {
            throw new IllegalArgumentException(String.format("%s: command name cannot be empty", command.getClass().getName()));
        }
        if (StringUtils.contains(command.getCallbackCommandName(), ":")) {
            throw new IllegalArgumentException(String.format("%s: command name '%s' cannot contain the symbol ':'", command.getClass().getName(), command.getCallbackCommandName()));
        }
    }

    @Override
    public boolean handleUpdate(AbsSender sender, Update update) throws Exception {
        if (!update.hasCallbackQuery()) {
            return false;
        }

        logger.debug("Start handler {}", CallbackQueryUpdateHandler.class.getName());

        UserSession userSession = SessionContext.getUserSession();
        userSession.setExternalType(ExternalType.TELEGRAM);
        userSession.setExternalId(String.valueOf(update.getCallbackQuery().getFrom().getId()));

        CallbackQuery callbackQuery = update.getCallbackQuery();
        String data = callbackQuery.getData();
//        String command = StringUtils.substringBefore(data, ":");
//        int index = data.indexOf(":");
        String commandName = StringUtils.substringBefore(data, ":");

        logger.debug("Callback command name: {}", commandName);

        CallbackCommand command = commands.getOrDefault(commandName, unknownCommand);
        command.execute(sender, callbackQuery);
        return true;
    }
}
