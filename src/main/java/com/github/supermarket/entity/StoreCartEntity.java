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
@Table(name = "STORE_CART")
public class StoreCartEntity implements PrimaryKey<Long> {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, columnDefinition = "Уникальный идентификатор")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID", nullable = false, columnDefinition = "Магазин")
    private StoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false, columnDefinition = "Покупатель")
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_DETAIL_ID", nullable = false, columnDefinition = "Продукт")
    private ProductDetailEntity productDetail;

    @Column(name = "QUANTITY", columnDefinition = "Количество товара")
    private BigDecimal quantity;

    @Column(name = "DISPLAY_ORDER", nullable = false, columnDefinition = "Порядок отображения")
    private Long displayOrder;
}

