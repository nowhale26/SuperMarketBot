package com.github.supermarket.entity;

import com.github.supermarket.common.domain.PrimaryKey;
import com.github.supermarket.entity.enumconverter.ExternalTypeConverter;
import com.github.supermarket.enums.ExternalType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "CUSTOMER")
public class CustomerEntity implements PrimaryKey<Long> {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, columnDefinition = "Уникальный идентификатор")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID", nullable = false, columnDefinition = "Магазин")
    private StoreEntity store;

    @Convert(converter = ExternalTypeConverter.class)
    @Column(name = "EXTERNAL_TYPE", nullable = false, columnDefinition = "Тип внешней системы")
    private ExternalType externalType;

    @Column(name = "EXTERNAL_ID", nullable = false, length = 250, columnDefinition = "Идентификатор покупателя во внешней системе")
    private String externalId;

    @Column(name = "SYSNAME", nullable = false, length = 250, columnDefinition = "Логин клиента во внешней системе")
    private String sysname;

    @Column(name = "NAME", length = 250, columnDefinition = "Имя клиента во внешней системе")
    private String name;

    @Column(name = "ADDRESS", length = 1000, columnDefinition = "Адрес")
    private String address;

    @Column(name = "EMAIL", length = 250, columnDefinition = "Email")
    private String email;

    @Column(name = "PHONE", length = 250, columnDefinition = "Телефон")
    private String phone;
}