package com.github.supermarket.telegram.command.catalog;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.product.ProductService;
import com.github.supermarket.service.product.model.Product;
import com.github.supermarket.service.product.model.ProductFilter;
import com.github.supermarket.service.product.model.ProductView;
import com.github.supermarket.telegram.command.product.ProductAddCallbackCommand;
import com.github.supermarket.telegram.command.product.ProductRemoveCallbackCommand;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import com.github.supermarket.telegram.handler.inline.InlineCommand;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CatalogProductInlineCommand implements InlineCommand {
    private final ProductService productService;
    private static final int CACHE_TIME = 1;

    public CatalogProductInlineCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute(AbsSender sender, InlineQuery inlineQuery) throws Exception {
        UserSession userSession = SessionContext.getUserSession();
        Long catalogId = 1L; //CallbackUtils.valueFromData(inlineQuery.getData(), Long.class);

        ProductFilter productFilter = new ProductFilter(userSession.getStoreId(), catalogId, null, null, null);
        ListResponse<ProductView> productListResponse = productService.getByParams(productFilter);

//        List<InlineQueryResult> inlineQueryResults = new ArrayList<>();
//        for (Product product : productListResponse.getItems()) {
//            inlineQueryResults.add(new InlineQueryResultArticle(product.getId().toString(), product.getName(), new InputTextMessageContent(product.getName())));
//        }
        List<InlineQueryResult> productsResult = new ArrayList<>();
        for (Product product : productListResponse.getItems()) {
            String description = StringUtils.isNotBlank(product.getDescription()) ? product.getDescription() : "-";
            productsResult.add(InlineQueryResultArticle.builder().id(product.getId().toString())
                    .thumbUrl(product.getImageUrl()).thumbHeight(48).thumbWidth(48)
                    .title(product.getName()).description(description)
                    .replyMarkup(createProductKeyboard(product))
                    .inputMessageContent(InputTextMessageContent.builder()
                            .parseMode("HTML").messageText(description)
                            .build())
                    .build());

        }

//        List<InlineQueryResult> inlineQueryResults = new ArrayList<>();
//        inlineQueryResults.add(new InlineQueryResultArticle("1", "Заголовок 1", new InputTextMessageContent("Текст 1")));
//        inlineQueryResults.add(new InlineQueryResultArticle("2", "Заголовок 2", new InputTextMessageContent("Текст 2")));
//        inlineQueryResults.add(new InlineQueryResultArticle("3", "Заголовок 3", new InputTextMessageContent("Текст 3")));

        sender.execute(
                AnswerInlineQuery.builder()
                        .inlineQueryId(inlineQuery.getId())
                        .results(productsResult)
                        .cacheTime(CACHE_TIME)
                        .build());

    }

    private InlineKeyboardMarkup createProductKeyboard(Product product) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(
                Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text("+")
                                .callbackData(CallbackUtils.toData(ProductAddCallbackCommand.ADD_PRODUCT_COMMAND, 1))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("-")
                                .callbackData(CallbackUtils.toData(ProductRemoveCallbackCommand.REMOVE_PRODUCT_COMMAND, 1L))
                                .build()));
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    @Override
    public String getInlineCommandName() {
        return "catalog";
    }
}
