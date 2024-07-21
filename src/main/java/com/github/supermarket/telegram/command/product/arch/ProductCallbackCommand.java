package com.github.supermarket.telegram.command.product.arch;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.catalog.CatalogService;
import com.github.supermarket.service.catalog.model.Catalog;
import com.github.supermarket.service.product.ProductService;
import com.github.supermarket.service.product.model.ProductFilter;
import com.github.supermarket.service.product.model.ProductView;
import com.github.supermarket.service.storecart.StoreCartService;
import com.github.supermarket.telegram.handler.callback.CallbackCommand;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.supermarket.telegram.command.common.EmptyCallbackCommand.EMPTY_CALLBACK_COMMAND;
import static com.github.supermarket.telegram.command.product.ProductAddCallbackCommand.ADD_PRODUCT_COMMAND;
import static com.github.supermarket.telegram.command.product.ProductRemoveCallbackCommand.REMOVE_PRODUCT_COMMAND;
import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.builder;

@Component
public class ProductCallbackCommand implements CallbackCommand {
    public static final String PRODUCT_CALLBACK_COMMAND = "product";

    private final ProductService productService;
    private final StoreCartService storeCartService;
    private final CatalogService catalogService;

    public ProductCallbackCommand(ProductService productService, StoreCartService storeCartService, CatalogService catalogService) {
        this.productService = productService;
        this.storeCartService = storeCartService;
        this.catalogService = catalogService;
    }

    @Override
    public void execute(AbsSender sender, CallbackQuery callbackQuery) throws Exception {
        UserSession userSession = SessionContext.getUserSession();
        Long catalogId = CallbackUtils.valueFromData(callbackQuery.getData(), Long.class);

        Catalog catalog = catalogService.get(catalogId);
        if (catalog != null) {
            Message message = sender.execute(SendMessage.builder()
                    .chatId(callbackQuery.getMessage().getChatId().toString())
                    .text(String.format("""
                            <b>%s:</b>
                            """, catalog.getName()))
                    .parseMode("html")
                    .build()
            );
        }

        ProductFilter productFilter = new ProductFilter(userSession.getStoreId(), catalogId, null, userSession.getExternalType(), userSession.getExternalId());
        ListResponse<ProductView> productListResponse = productService.getByParams(productFilter);
        for (ProductView product : productListResponse.getItems()) {
            if (StringUtils.isNotBlank(product.getImageUrl())) {
                URL url = new URL(product.getImageUrl());
                InputFile inputFile = new InputFile(url.openStream(), "name");
                sender.execute(SendPhoto.builder()
                        .chatId(callbackQuery.getMessage().getChatId().toString())
                        .photo(inputFile)
                        .caption(String.format("""
                                <b>%s</b>
                                <i>%,.2f руб.</i>
                                %s
                                """, product.getName(), product.getPrice(), product.getDescription()))
                        .parseMode("html")
                        .replyMarkup(createProductKeyboard(product))
                        .build()
                );
            } else {
                Message message = sender.execute(SendMessage.builder()
                        .chatId(callbackQuery.getMessage().getChatId().toString())
                        .text(String.format("""
                                <b>%s</b>
                                <i>%,.2f руб.</i>
                                %s
                                """, product.getName(), product.getPrice(), product.getDescription()))
                        .parseMode("html")
                        .replyMarkup(createProductKeyboard(product))
                        .build()
                );
            }
        }
    }

    private InlineKeyboardMarkup createProductKeyboard(ProductView product) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(
                Optional.ofNullable(product.getStoreCartQuantity())
                        .filter(quantity -> ObjectUtils.compare(quantity, BigDecimal.ZERO) > 0).map(quantity -> List.of(
                                builder().text("+").callbackData(CallbackUtils.toData(ADD_PRODUCT_COMMAND, product.getProductDetailId())).build(),
                                builder().text(NumberFormat.getInstance().format(quantity)).callbackData(CallbackUtils.toData(EMPTY_CALLBACK_COMMAND, product.getProductDetailId())).build(),
                                builder().text("-").callbackData(CallbackUtils.toData(REMOVE_PRODUCT_COMMAND, product.getProductDetailId())).build())
                        ).orElseGet(() -> List.of(
                                builder().text(NumberFormat.getInstance().format(product.getPrice()) + " руб.").callbackData(CallbackUtils.toData(ADD_PRODUCT_COMMAND, product.getProductDetailId())).build())
                        )
        );
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    @Override
    public String getCallbackCommandName() {
        return PRODUCT_CALLBACK_COMMAND;
    }
}
