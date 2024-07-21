package com.github.supermarket.telegram.command.catalog.arch;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.product.ProductService;
import com.github.supermarket.service.product.model.ProductFilter;
import com.github.supermarket.service.product.model.ProductView;
import com.github.supermarket.service.storecart.StoreCartService;
import com.github.supermarket.telegram.command.product.ProductAddCallbackCommand;
import com.github.supermarket.telegram.command.product.ProductRemoveCallbackCommand;
import com.github.supermarket.telegram.handler.callback.CallbackCommand;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import com.github.supermarket.telegram.session.SessionContext;
import com.github.supermarket.telegram.session.UserSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CatalogProductCallbackCommandOld implements CallbackCommand {
    private final ProductService productService;
    private final StoreCartService storeCartService;
    private static final int CACHE_TIME = 1;

    public CatalogProductCallbackCommandOld(ProductService productService, StoreCartService storeCartService) {
        this.productService = productService;
        this.storeCartService = storeCartService;
    }

    @Override
    public void execute(AbsSender sender, CallbackQuery callbackQuery) throws Exception {
        UserSession userSession = SessionContext.getUserSession();
        Long storeId = userSession.getStoreId();
        Long catalogId = CallbackUtils.valueFromData(callbackQuery.getData(), Long.class);

        ProductFilter productFilter = new ProductFilter(storeId, catalogId, null, userSession.getExternalType(), userSession.getExternalId());
        ListResponse<ProductView> productListResponse = productService.getByParams(productFilter);

//        Map<Long, Product> storeCartProducts = storeCartService.getProductByParams(new StoreCartFilter(storeId, null, userSession.getExternalType(), userSession.getExternalId(), null, null));

//        List<InlineQueryResult> inlineQueryResults = new ArrayList<>();
//        for (Product product : productListResponse.getItems()) {
//            inlineQueryResults.add(new InlineQueryResultArticle(product.getId().toString(), product.getName(), new InputTextMessageContent(product.getName())));
//        }
        List<InlineQueryResult> productsResult = new ArrayList<>();
        for (ProductView product : productListResponse.getItems()) {
//            String description = StringUtils.isNotBlank(product.getDescription()) ? product.getDescription() : "-";
//            productsResult.add(InlineQueryResultArticle.builder().id(product.getId().toString())
//                    .thumbUrl(product.getImageUrl()).thumbHeight(48).thumbWidth(48)
//                    .title(product.getName()).description(description)
//                    .replyMarkup(createProductKeyboard(product))
//                    .inputMessageContent(InputTextMessageContent.builder()
//                            .parseMode("HTML").messageText(description)
//                            .build())
//                    .build());

//            sender.execute(SendInvoice.builder()
//                    .title(product.getName())
//                    .description(product.getDescription())
//                    .payload("toy:" + product.getId())
//                    .photoUrl(product.getImageUrl())
//                    .photoHeight(300)
//                    .photoWidth(300)
//                    .startParameter("startParam")
//                    .providerToken("Stripe TEST MODE")
//                    .currency("RUB")
//                    .needEmail(true)
//                    .needName(true)
//                    .needPhoneNumber(true)
//                    .needShippingAddress(true)
//                    .maxTipAmount(2000)
//                    .suggestedTipAmount(1000)
//                    .price(LabeledPrice.builder().label("Цена товара").amount(product.getPrice().intValue()).build())
////                    .price(LabeledPrice.builder().label("Доставка").amount(random.nextInt(10) * 100).build())
////                    .price(LabeledPrice.builder().label("Налог (10%)").amount(toy.getPrice() / 10).build())
//                    .chatId(callbackQuery.getMessage().getChatId().toString())
//                    .replyMarkup(
//                            InlineKeyboardMarkup.builder()
//                                    .keyboardRow(
//                                            Collections.singletonList(
//                                                    InlineKeyboardButton.builder().text("Купи игрушку!").pay(true).build()))
//                                    .keyboardRow(
//                                            Collections.singletonList(
//                                                    InlineKeyboardButton.builder()
//                                                            .text("Другой товар")
//                                                            .callbackData("123")
//                                                            .build()))
//                                    .build())
//                    .build());

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
                sender.execute(SendMessage.builder()
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


//            sender.execute(SendMessage.builder()
//                    .chatId(String.valueOf(callbackQuery.getMessage().getChatId()))
//                    .text(product.getName())
//                    .replyMarkup(createProductKeyboard(product))
//                    .build()
//            );
        }

//        List<InlineQueryResult> inlineQueryResults = new ArrayList<>();
//        inlineQueryResults.add(new InlineQueryResultArticle("1", "Заголовок 1", new InputTextMessageContent("Текст 1")));
//        inlineQueryResults.add(new InlineQueryResultArticle("2", "Заголовок 2", new InputTextMessageContent("Текст 2")));
//        inlineQueryResults.add(new InlineQueryResultArticle("3", "Заголовок 3", new InputTextMessageContent("Текст 3")));

//        sender.execute(
//                AnswerInlineQuery.builder()
//                        .inlineQueryId(callbackQuery.getId())
//                        .results(productsResult)
//                        .cacheTime(CACHE_TIME)
//                        .build());

    }

    private InlineKeyboardMarkup createProductKeyboard(ProductView product) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<List<InlineKeyboardButton>>();
        if (product.getStoreCartQuantity() != null) {
            rows.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text("-")
                                    .callbackData(CallbackUtils.toData(ProductAddCallbackCommand.ADD_PRODUCT_COMMAND, product.getId()))
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(product.getStoreCartQuantity().toString())
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text("+")
                                    .callbackData(CallbackUtils.toData(ProductRemoveCallbackCommand.REMOVE_PRODUCT_COMMAND, product.getId()))
                                    .build())
            );
        } else {
            rows.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(product.getPrice().toString() + " руб.")
                                    .callbackData(CallbackUtils.toData(ProductAddCallbackCommand.ADD_PRODUCT_COMMAND, product.getId()))
                                    .build())
            );
        }
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    @Override
    public String getCallbackCommandName() {
        return "catalogOld";
    }
}
