package com.github.supermarket.service.storecart.model;

import com.github.supermarket.service.product.model.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 *  Добавление/удаление товара в магазине
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class StoreCartDelta {
    /**
     * Идентификатор магазина
     */
    @NotNull
    private Long storeId;

    /**
     * Идентификатор покупателя
     */
    @NotNull
    private Long customerId;

    /**
     * Продукт
     */
    @NotNull
    private Long productDetailId;

    /**
     * Изменяемое количество товара
     */
    @NotNull
    @Positive
    private BigDecimal delta;
}