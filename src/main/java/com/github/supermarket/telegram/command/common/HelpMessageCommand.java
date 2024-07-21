package com.github.supermarket.telegram.command.common;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.catalog.CatalogService;
import com.github.supermarket.service.catalog.model.Catalog;
import com.github.supermarket.service.catalog.model.CatalogFilter;
import com.github.supermarket.telegram.command.bot.BaseCommand;
import com.github.supermarket.telegram.handler.message.MessageCommand;
import com.github.supermarket.telegram.session.SessionContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Help {@link MessageCommand}.
 */
@Component
public class HelpMessageCommand implements MessageCommand {
    private final CatalogService catalogService;

    public HelpMessageCommand(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Override
    public void execute(AbsSender sender, Message message) throws TelegramApiException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>Выбор каталога</b>\n");
        ListResponse<Catalog> catalogResponse = catalogService.getByParams(CatalogFilter.builder().storeId(SessionContext.getUserSession().getStoreId()).build());
        for (Catalog catalog : catalogResponse.getItems()) {
            stringBuilder.append("    ").append("/").append(catalog.getSysname().toLowerCase()).append(" - ").append(catalog.getName()).append("\n");
        }

        stringBuilder.append("<b>Команды</b>\n");
        for (BaseCommand baseCommand : BaseCommand.values()) {
            stringBuilder.append("    ").append("/").append(baseCommand.getCommand()).append(" - ").append(baseCommand.getDescription()).append("\n");
        }

        sender.execute(SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(stringBuilder.toString())
                .parseMode("html")
                .build()
        );
    }

    @Override
    public String getMessageCommandName() {
        return "/help";
    }
}