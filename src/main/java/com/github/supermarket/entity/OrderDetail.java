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
@Table(name = "ORDER_DETAIL")
public class OrderDetail implements PrimaryKey<Long> {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, columnDefinition = "Уникальный идентификатор")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false, columnDefinition = "Заказ")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_DETAIL_ID", nullable = false, columnDefinition = "Продукт")
    private ProductDetailEntity productDetail;

    @Column(name = "NAME", nullable = false, length = 250, columnDefinition = "Название товара на дату заказа")
    private String name;

    @Column(name = "PRICE", columnDefinition = "Стоимость товара на дату заказа")
    private BigDecimal price;

    @Column(name = "QUANTITY", columnDefinition = "Количество товара")
    private BigDecimal quantity;

    @Column(name = "DISPLAY_ORDER", nullable = false, columnDefinition = "Порядок отображения")
    private Long displayOrder;
}
