package com.github.supermarket.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductView extends Product {
    /**
     *  Уникальный идентификатор детальной информации о продукте
     */
    private Long productDetailId;

    /**
     * Стоимость товара
     */
    private BigDecimal price;

    /**
     * Количество товара в корзине
     */
    private BigDecimal storeCartQuantity;
}
