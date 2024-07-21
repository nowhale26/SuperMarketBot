package com.github.supermarket.telegram.command.product;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.catalog.CatalogService;
import com.github.supermarket.service.catalog.model.Catalog;
import com.github.supermarket.service.catalog.model.CatalogFilter;
import com.github.supermarket.service.product.ProductService;
import com.github.supermarket.service.product.model.ProductFilter;
import com.github.supermarket.service.product.model.ProductView;
import com.github.supermarket.service.storecart.StoreCartService;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import com.github.supermarket.telegram.handler.message.MessageCommand;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.supermarket.telegram.command.common.EmptyCallbackCommand.EMPTY_CALLBACK_COMMAND;
import static com.github.supermarket.telegram.command.common.NoMessageCommand.BASE_MESSAGE;
import static com.github.supermarket.telegram.command.product.ProductAddCallbackCommand.ADD_PRODUCT_COMMAND;
import static com.github.supermarket.telegram.command.product.ProductRemoveCallbackCommand.REMOVE_PRODUCT_COMMAND;
import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.builder;

@Component
public class ProductMessageCommand implements MessageCommand {
    private static String COMMAND_PREFIX = "/";
    private static final String EMPTY_COMMAND = "Вы ввели пустую команду.\n";
    private static final String NOT_FOUND_COMMAND = "Команда или каталог не найдены.\n";

    private final ProductService productService;
    private final CatalogService catalogService;
    private final ProductBuilder productBuilder;

    public ProductMessageCommand(ProductService productService, CatalogService catalogService, ProductBuilder productBuilder) {
        this.productService = productService;
        this.catalogService = catalogService;
        this.productBuilder = productBuilder;
    }

    @Override
    public void execute(AbsSender sender, Message message) throws Exception {
        UserSession userSession = SessionContext.getUserSession();

        String text = StringUtils.trimToEmpty(message.getText());
        String catalogSysName = text.split(" ")[0].toLowerCase();
        if (catalogSysName.startsWith(COMMAND_PREFIX)) {
            catalogSysName = catalogSysName.substring(1);
        }
        if (StringUtils.isEmpty(catalogSysName)) {
            sender.execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text(EMPTY_COMMAND + BASE_MESSAGE)
                    .parseMode("html")
                    .build()
            );
            return;
        }

        ListResponse<Catalog> catalogListResponse = catalogService.getByParams(CatalogFilter.builder().storeId(userSession.getStoreId()).sysname(catalogSysName).build());
        if (catalogListResponse.getItems().isEmpty()) {
            sender.execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text(NOT_FOUND_COMMAND + BASE_MESSAGE)
                    .parseMode("html")
                    .build()
            );
            return;
        }

        Catalog catalog = catalogListResponse.getItems().get(0);
        sender.execute(SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(String.format("<b>%s:</b>", catalog.getName()))
                .parseMode("html")
                .build()
        );

        ProductFilter productFilter = ProductFilter.builder()
                .storeId(userSession.getStoreId())
                .catalogId(catalog.getId())
                .externalType(userSession.getExternalType())
                .externalId(userSession.getExternalId())
                .build();

        ListResponse<ProductView> productListResponse = productService.getByParams(productFilter);
        for (ProductView product : productListResponse.getItems()) {
            if (StringUtils.isNotBlank(product.getImageUrl())) {
                URL url = new URL(product.getImageUrl());
                InputFile inputFile = new InputFile(url.openStream(), "name");
                sender.execute(SendPhoto.builder()
                        .chatId(message.getChatId().toString())
                        .photo(inputFile)
                        .caption(productBuilder.buildText(product))
                        .parseMode("html")
                        .replyMarkup(productBuilder.buildButtons(product.getProductDetailId(), product.getPrice(), product.getStoreCartQuantity()))
                        .build()
                );
            } else {
                sender.execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text(productBuilder.buildText(product))
                        .parseMode("html")
                        .replyMarkup(productBuilder.buildButtons(product.getProductDetailId(), product.getPrice(), product.getStoreCartQuantity()))
                        .build()
                );
            }
        }

    }

    @Override
    public String getMessageCommandName() {
        return "/product";
    }
}

