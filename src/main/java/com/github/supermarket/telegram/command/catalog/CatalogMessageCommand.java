package com.github.supermarket.telegram.command.catalog;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.catalog.CatalogService;
import com.github.supermarket.service.catalog.model.Catalog;
import com.github.supermarket.service.catalog.model.CatalogFilter;
import com.github.supermarket.telegram.handler.message.MessageCommand;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
public class CatalogMessageCommand implements MessageCommand {
    private final CatalogService catalogService;
    private static final int CACHE_TIME = 10;

    public CatalogMessageCommand(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Override
    public void execute(AbsSender sender, Message message) throws Exception {
        UserSession userSession = SessionContext.getUserSession();
        CatalogFilter catalogFilter = CatalogFilter.builder().storeId(userSession.getStoreId()).build();
        ListResponse<Catalog> catalogListResponse = catalogService.getByParams(catalogFilter);

        List<InlineQueryResult> inlineQueryResults = new ArrayList<>();
        for (Catalog catalog : catalogListResponse.getItems()) {
            inlineQueryResults.add(new InlineQueryResultArticle(catalog.getId().toString(), catalog.getName(), new InputTextMessageContent(catalog.getName())));
        }

        sender.execute(
                AnswerInlineQuery.builder()
                        .inlineQueryId(message.getChatId().toString())
                        .results(inlineQueryResults)
//                        .cacheTime(CACHE_TIME)
                        .build());

    }

    @Override
    public String getMessageCommandName() {
        return "каталог";
    }
}
