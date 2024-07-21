package com.github.supermarket.entity;

import com.github.supermarket.common.domain.PrimaryKey;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "PRODUCT_DETAIL")
public class ProductDetailEntity implements PrimaryKey<Long> {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, columnDefinition = "Уникальный идентификатор")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false, columnDefinition = "Продукт")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SIZE_TYPE_ID", nullable = false, columnDefinition = "Размерность продукта")
    private SizeTypeEntity sizeType;

    @Column(name = "PRICE", columnDefinition = "Стоимость товара")
    private BigDecimal price;

    @Column(name = "QUANTITY", columnDefinition = "Количество товара")
    private BigDecimal quantity;

    @Column(name = "IS_DEFAULT", nullable = false, columnDefinition = "Признак, что размерность по умолчанию (для отображения цены в списках продуктов).")
    private Boolean isDefault;
}