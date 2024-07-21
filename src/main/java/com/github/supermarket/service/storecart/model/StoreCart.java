package com.github.supermarket.service.storecart.model;

import com.github.supermarket.service.product.model.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class StoreCart {
    /**
     * Уникальный идентификатор
     */
    private Long id;

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
    private ProductDetail productDetail;

    /**
     * Количество товара
     */
    @NotNull
    private BigDecimal quantity;

    /**
     *  Порядок отображения
     */
    @NotNull
    private Long displayOrder;
}