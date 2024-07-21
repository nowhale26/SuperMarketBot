package com.github.supermarket.telegram.command.product;

import com.github.supermarket.service.product.model.Product;
import com.github.supermarket.service.product.model.ProductDetail;
import com.github.supermarket.service.product.model.ProductView;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.supermarket.telegram.command.basket.BasketCallbackCommand.BASKET_COMMAND;
import static com.github.supermarket.telegram.command.common.EmptyCallbackCommand.EMPTY_CALLBACK_COMMAND;
import static com.github.supermarket.telegram.command.product.BaseProductCallbackCommand.ADD_PRODUCT_COMMAND;
import static com.github.supermarket.telegram.command.product.BaseProductCallbackCommand.REMOVE_PRODUCT_COMMAND;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.builder;

@Component
public class ProductBuilder {

    public String buildText(ProductView productView) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.##");
        return String.format("""
                        <b>%s</b>  <i>%s руб.</i>
                                                        
                        %s
                        """
                , productView.getName()
                , decimalFormat.format(productView.getPrice())
                , productView.getDescription());
    }

    public InlineKeyboardMarkup buildButtons(Long productDetailId, BigDecimal price, BigDecimal productQuantity) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<List<InlineKeyboardButton>>();
        DecimalFormat decimalFormat = new DecimalFormat("#0.##");
//        🛒
        rows.add(
                Optional.ofNullable(productQuantity)
                        .filter(quantity -> ObjectUtils.compare(quantity, BigDecimal.ZERO) > 0).map(quantity -> List.of(
                                builder().text("+").callbackData(CallbackUtils.toData(ADD_PRODUCT_COMMAND, productDetailId)).build(),
                                builder().text(decimalFormat.format(quantity)).callbackData(CallbackUtils.toData(EMPTY_CALLBACK_COMMAND, productDetailId)).build(),
                                builder().text("-").callbackData(CallbackUtils.toData(REMOVE_PRODUCT_COMMAND, productDetailId)).build(),
                                builder().text("Корзина").callbackData(CallbackUtils.toData(BASKET_COMMAND, productDetailId)).build())
                        ).orElseGet(() -> List.of(
                                builder().text(decimalFormat.format(price) + " руб.").callbackData(CallbackUtils.toData(ADD_PRODUCT_COMMAND, productDetailId)).build())
                        )
        );
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }


}
