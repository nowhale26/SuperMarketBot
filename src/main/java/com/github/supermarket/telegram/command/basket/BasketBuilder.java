package com.github.supermarket.telegram.command.basket;

import com.github.supermarket.service.product.model.Product;
import com.github.supermarket.service.product.model.ProductDetail;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.github.supermarket.telegram.handler.callback.CallbackUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.supermarket.telegram.command.basket.BaseBasketCallbackCommand.ADD_BASKET_COMMAND;
import static com.github.supermarket.telegram.command.basket.BaseBasketCallbackCommand.REMOVE_BASKET_COMMAND;
import static com.github.supermarket.telegram.command.common.EmptyCallbackCommand.EMPTY_CALLBACK_COMMAND;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.builder;

@Component
public class BasketBuilder {

    public String buildText(StoreCart storeCart) {
        Optional<ProductDetail> productDetail = ofNullable(storeCart.getProductDetail());
        String name = productDetail.map(ProductDetail::getProduct).map(Product::getName).orElse(EMPTY);
        BigDecimal price = productDetail.map(ProductDetail::getPrice).orElse(BigDecimal.ZERO);
        BigDecimal quantity = ofNullable(storeCart.getQuantity()).orElse(BigDecimal.ZERO);
        BigDecimal amount = price.multiply(quantity);
        String description = productDetail.map(ProductDetail::getProduct).map(Product::getDescription).orElse(EMPTY);

        DecimalFormat decimalFormat = new DecimalFormat("#0.##");
        return String.format("""
                        <b>%s</b>  <i>%s руб. * %s = %s руб.</i>
                                                        
                        %s
                        """
                , name
                , decimalFormat.format(price)
                , decimalFormat.format(quantity)
                , decimalFormat.format(amount)
                , description);
    }

    public InlineKeyboardMarkup buildButtons(StoreCart storeCart) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<List<InlineKeyboardButton>>();
        Optional<ProductDetail> productDetail = ofNullable(storeCart.getProductDetail());
        BigDecimal price = productDetail.map(ProductDetail::getPrice).orElse(BigDecimal.ZERO);
        Long productDetailId = productDetail.map(ProductDetail::getId).orElse(NumberUtils.LONG_ZERO);
        DecimalFormat decimalFormat = new DecimalFormat("#0.##");
        rows.add(
                ofNullable(storeCart.getQuantity())
                        .filter(quantity -> ObjectUtils.compare(quantity, BigDecimal.ZERO) > 0).map(quantity -> List.of(
                                builder().text("+").callbackData(CallbackUtils.toData(ADD_BASKET_COMMAND, productDetailId)).build(),
                                builder().text(NumberFormat.getInstance().format(quantity)).callbackData(CallbackUtils.toData(EMPTY_CALLBACK_COMMAND, productDetailId)).build(),
                                builder().text("-").callbackData(CallbackUtils.toData(REMOVE_BASKET_COMMAND, productDetailId)).build())
                        ).orElseGet(() -> List.of(
                                builder().text(decimalFormat.format(price) + " руб.").callbackData(CallbackUtils.toData(ADD_BASKET_COMMAND, productDetailId)).build())
                        )
        );
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

}
