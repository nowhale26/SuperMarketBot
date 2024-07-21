package com.github.supermarket.telegram.command.product.arch;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.service.product.ProductService;
import com.github.supermarket.service.product.model.ProductFilter;
import com.github.supermarket.service.product.model.ProductView;
import com.github.supermarket.telegram.command.product.ProductAddCallbackCommand;
import com.github.supermarket.telegram.command.product.ProductRemoveCallbackCommand;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import com.github.supermarket.telegram.handler.message.MessageCommand;
import com.github.supermarket.telegram.session.SessionContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ProductMessageCommandOld implements MessageCommand {
    private final ProductService productService;

    public ProductMessageCommandOld(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute(AbsSender sender, Message message) throws Exception {
        ProductFilter productFilter = ProductFilter.builder().storeId(SessionContext.getUserSession().getStoreId()).build();
        ListResponse<ProductView> productListResponse = productService.getByParams(productFilter);
        String PRODUCT_MESSAGE = "";


        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(
                Arrays.asList(
                        InlineKeyboardButton.builder().text("External Link").url("www.google.com").build(),
                        InlineKeyboardButton.builder()
                                .text("Internal Link")
                                .url("https://t.me/BotFather")
                                .build()));
        buttons.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Inline query")
                                .switchInlineQueryCurrentChat("Concurrent")
                                .build()));

        buttons.add(
                Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text("+")
                                .callbackData(CallbackUtils.toData(ProductAddCallbackCommand.ADD_PRODUCT_COMMAND, 1))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("-")
                                .callbackData(CallbackUtils.toData(ProductRemoveCallbackCommand.REMOVE_PRODUCT_COMMAND, 1L))
                                .build()));

        //        buttons.add(
//                Arrays.asList(
//                        InlineKeyboardButton.builder()
//                                .text("Alert")
//                                .callbackData(StringUtil.serialize(new AlertDto("Message 1", true)))
//                                .build(),
//                        InlineKeyboardButton.builder()
//                                .text("Simple Alert")
//                                .callbackData(StringUtil.serialize(new AlertDto("Message 2", false)))
//                                .build()));
//        buttons.add(
//                Collections.singletonList(
//                        InlineKeyboardButton.builder()
//                                .text("Edit Message")
//                                .callbackData(StringUtil.serialize(new RandomMessageDto(0)))
//                                .build()));

        for(ProductView product : productListResponse.getItems()) {
            String product_price=product.getPrice().toString();
            PRODUCT_MESSAGE=product.getName()+"\n"+product.getDescription()+"\n"+product_price.substring(0,product_price.indexOf('.'))+" рублей"+"\n";
            InputFile new_photo = new InputFile().setMedia(new FileInputStream("D:\\Users\\User\\temp\\butter_dog.jpg"), "new_photo");
            sender.execute(SendPhoto.builder().chatId(message.getChatId().toString()).photo(new_photo).caption(PRODUCT_MESSAGE).parseMode("html").build());
        }




       // sender.execute(
       //         SendMessage.builder()
       //                 .text(PRODUCT_MESSAGE)
       //                 .parseMode("html")
       //                 .chatId(message.getChatId().toString())
       //                 .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
       //                 .build());

    }

    @Override
    public String getMessageCommandName() {
        return "/productOld";
    }
}

