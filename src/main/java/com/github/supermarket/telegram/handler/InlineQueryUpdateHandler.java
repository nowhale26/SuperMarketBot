package com.github.supermarket.telegram.handler;

import com.github.supermarket.enums.ExternalType;
import com.github.supermarket.telegram.command.catalog.CatalogInlineCommand;
import com.github.supermarket.telegram.command.common.UnknownInlineCommand;
import com.github.supermarket.telegram.handler.inline.InlineCommand;
import com.github.supermarket.telegram.handler.message.MessageCommand;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
public class InlineQueryUpdateHandler implements UpdateHandler {
    private static Logger logger = LoggerFactory.getLogger(InlineQueryUpdateHandler.class);

    public static String COMMAND_PREFIX = "/";

    private final ImmutableMap<String, InlineCommand> commands;
    private final UnknownInlineCommand unknownCommand;
    private final CatalogInlineCommand catalogInlineCommand;

    public InlineQueryUpdateHandler(List<InlineCommand> inlineCommands, UnknownInlineCommand unknownCommand, CatalogInlineCommand catalogInlineCommand) {
        ImmutableMap.Builder builder = ImmutableMap.<String, MessageCommand>builder();
        for (InlineCommand command : inlineCommands) {
            validate(command);
            builder.put(command.getInlineCommandName(), command);
        }
        commands = builder.build();
        this.unknownCommand = unknownCommand;
        this.catalogInlineCommand = catalogInlineCommand;
    }

    private void validate(InlineCommand command) {
        if (StringUtils.isBlank(command.getInlineCommandName())) {
            throw new IllegalArgumentException(String.format("%s: command name cannot be empty", command.getClass().getName()));
        }
        if (StringUtils.contains(command.getInlineCommandName(), ":")) {
            throw new IllegalArgumentException(String.format("%s: command name '%s' cannot contain the symbol ':'", command.getClass().getName(), command.getInlineCommandName()));
        }
    }

    @Override
    public boolean handleUpdate(AbsSender sender, Update update) throws Exception {
        if (!update.hasInlineQuery()) {
            return false;
        }
        if (update.getInlineQuery().getQuery() == null) {
            return false;
        }
        logger.debug("Start handler {}", InlineQueryUpdateHandler.class.getName());

        UserSession userSession = SessionContext.getUserSession();
        userSession.setExternalType(ExternalType.TELEGRAM);
        userSession.setExternalId(String.valueOf(update.getInlineQuery().getFrom().getId()));

        String query = update.getInlineQuery().getQuery().trim();
        String commandName = query.split(" ")[0].toLowerCase();

        logger.debug("Inline command name: {}", commandName);

        InlineCommand messageCommand = commands.getOrDefault(commandName, unknownCommand);
        messageCommand.execute(sender, update.getInlineQuery());

//        String query = update.getInlineQuery().getQuery().trim();
//        if (query.startsWith(COMMAND_PREFIX)) {
//            String commandName = query.split(" ")[0].toLowerCase();
//            InlineCommand messageCommand = commands.getOrDefault(commandName, unknownCommand);
//            messageCommand.execute(update.getInlineQuery());
//        } else {
//            catalogInlineCommand.execute(update.getInlineQuery());
//        }

//        String query = update.getInlineQuery().getQuery();
//        String commandName = InlineUtils.commandNameFromQuery(query);
//        InlineCommand command = commands.getOrDefault(commandName, unknownCommand);
//        command.execute(update.getInlineQuery());
        return true;
    }
}
