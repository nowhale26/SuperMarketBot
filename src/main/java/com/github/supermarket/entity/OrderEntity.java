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
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ORDER")
public class OrderEntity implements PrimaryKey<Long> {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, columnDefinition = "Уникальный идентификатор")
    private Long id;

    @Column(name = "CREATE_DATE", nullable = false, columnDefinition = "Дата создания заказа")
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false, columnDefinition = "Покупатель")
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID", nullable = false, columnDefinition = "Организация")
    private OrgEntity org;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID", nullable = false, columnDefinition = "Магазин")
    private StoreEntity store;

    @Column(name = "AMOUNT", columnDefinition = "Сумма покупки")
    private BigDecimal amount;

    @Column(name = "CURRENCY", length = 10, columnDefinition = "Валюта покупки. Обозначение валюты в платежной системе (например: трех буквенное по стандарту ISO 4217)")
    private String currency;

    @Column(name = "IS_PAYMENT", nullable = false, columnDefinition = "Признак, что покупка оплачена. true - оплачена")
    private Boolean isPayment;

    @Column(name = "ADDRESS", length = 1000, columnDefinition = "Адрес доставки")
    private String address;

    @Column(name = "EMAIL", length = 250, columnDefinition = "Email покупателя")
    private String email;

    @Column(name = "PHONE", length = 250, columnDefinition = "Телефон покупателя")
    private String phone;

    @Column(name = "COMMENT", length = 500, columnDefinition = "Комментарий покупателя")
    private String comment;
}
