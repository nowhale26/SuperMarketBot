package com.github.supermarket.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductDetail implements Serializable {
    /**
     *  Уникальный идентификатор
     */
    private Long id;

    /**
     *  Продукт
     */
    private Product product;

    /**
     *  Размерность продукта
     */
    private Long sizeTypeId;

    /**
     * Стоимость товара
     */
    private BigDecimal price;

    /**
     *  Количество товара
     */
    private BigDecimal quantity;

    /**
     * Признак, по умолчанию (для отображения цены в списках продуктов).
     */
    private Boolean isDefault;
}
