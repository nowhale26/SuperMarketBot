package com.github.supermarket.telegram.handler;

import com.github.supermarket.enums.ExternalType;
import com.github.supermarket.telegram.command.common.NoMessageCommand;
import com.github.supermarket.telegram.command.common.arch.UnknownMessageCommand;
import com.github.supermarket.telegram.command.product.ProductMessageCommand;
import com.github.supermarket.telegram.handler.message.MessageCommand;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
public class MessageUpdateHandler implements UpdateHandler {
    private static Logger logger = LoggerFactory.getLogger(MessageUpdateHandler.class);

    public static String COMMAND_PREFIX = "/";
    private final ImmutableMap<String, MessageCommand> commands;
//    private final UnknownMessageCommand unknownCommand;
    private final NoMessageCommand noMessageCommand;
    private final ProductMessageCommand productMessageCommand;

    public MessageUpdateHandler(List<MessageCommand> messageMessageCommands, NoMessageCommand noMessageCommand, ProductMessageCommand productMessageCommand) {
        ImmutableMap.Builder builder = ImmutableMap.<String, MessageCommand>builder();
        for (MessageCommand messageCommand : messageMessageCommands) {
            builder.put(messageCommand.getMessageCommandName(), messageCommand);
        }
        commands = builder.build();
//        this.unknownCommand = unknownCommand;
        this.noMessageCommand = noMessageCommand;
        this.productMessageCommand = productMessageCommand;
    }

    @Override
    public boolean handleUpdate(AbsSender sender, Update update) throws Exception {
        if (!update.hasMessage()) {
            return false;
        }
        if (!update.getMessage().hasText()) {
            return false;
        }

        logger.debug("Start handler {}", MessageUpdateHandler.class.getName());

        UserSession userSession = SessionContext.getUserSession();
        userSession.setExternalType(ExternalType.TELEGRAM);
        userSession.setExternalId(String.valueOf(update.getMessage().getFrom().getId()));

        String message = update.getMessage().getText().trim();

//        String commandName = message.split(" ")[0].toLowerCase();
//        logger.debug("Message command name: {}", commandName);
//        MessageCommand messageCommand = commands.getOrDefault(commandName, productMessageCommand);
//        messageCommand.execute(sender, update.getMessage());
//        return true;

        if (message.startsWith(COMMAND_PREFIX)) {
            String commandName = message.split(" ")[0].toLowerCase();
            logger.debug("Message command name: {}", commandName);
            MessageCommand messageCommand = commands.getOrDefault(commandName, productMessageCommand);
            messageCommand.execute(sender, update.getMessage());
//            return true;
        } else {
//            return false;
            noMessageCommand.execute(sender, update.getMessage());
        }
        return true;
    }

//    protected boolean isNeedHandle(Update update) {
//        if (!update.hasMessage()) {
//            return false;
//        }
//        if (!update.getMessage().hasText()) {
//            return false;
//        }
//        return true;
//    }
//
//    protected void initUserSession(Update update) {
//        UserSession userSession = SessionContext.getUserSession();
//        userSession.setExternalType(ExternalType.TELEGRAM);
//        userSession.setExternalId(String.valueOf(update.getMessage().getFrom().getId()));
//    }


}
