package com.github.supermarket.telegram;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.exception.ChatBotException;
import com.github.supermarket.service.store.StoreService;
import com.github.supermarket.service.store.model.Store;
import com.github.supermarket.service.store.model.StoreFilter;
import com.github.supermarket.telegram.command.bot.SupermarketBotCommand;
import com.github.supermarket.telegram.handler.UpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Component
public class SupermarketBotInitializer implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(SupermarketBotInitializer.class);

    private final List<UpdateHandler> updateHandlers;
    private final StoreService storeService;
    private final SupermarketBotCommand supermarketBotCommand;

    public SupermarketBotInitializer(@Lazy List<UpdateHandler> updateHandlers, StoreService storeService, SupermarketBotCommand supermarketBotCommand) {
        this.updateHandlers = updateHandlers;
        this.storeService = storeService;
        this.supermarketBotCommand = supermarketBotCommand;
    }

    @Override
    public void afterPropertiesSet() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            StoreFilter storeFilter = new StoreFilter();
            storeFilter.setTelegramIsActive(Boolean.TRUE);
            ListResponse<Store> stores = storeService.getByParams(storeFilter);
            stores.getItems().forEach(store -> {
                try {
                    SupermarketBot supermarketBot = new SupermarketBot(store.getTelegramUsername(), store.getTelegramToken(), store.getId(), updateHandlers);
                    telegramBotsApi.registerBot(supermarketBot);
                    supermarketBotCommand.init(supermarketBot, store.getId());
                } catch (TelegramApiException e) {
                    logger.error("Bot telegram registration error {}", store.getTelegramUsername(), e);
                }
            });
        } catch (TelegramApiException e) {
            throw new ChatBotException(e);
        }
    }
}
