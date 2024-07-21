package com.github.supermarket.service.customer.model;

import com.github.supermarket.enums.ExternalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer {
    /**
     *  Уникальный идентификатор
     */
    private Long id;

    /**
     * Идентификатор магазина
     */
    @NotNull
    private Long storeId;

    /**
     * Тип внешней системы
     */
    @NotNull
    private ExternalType externalType;

    /**
     * Идентификатор покупателя во внешней системе
     */
    @NotNull
    private String externalId;

    /**
     * Логин клиента во внешней системе
     */
    @NotNull
    private String sysname;

    /**
     * Имя клиента во внешней системе
     */
    private String name;

    /**
     * Адрес
     */
    private String address;

    /**
     * Email
     */
    private String email;

    /**
     * Телефон
     */
    private String phone;
}