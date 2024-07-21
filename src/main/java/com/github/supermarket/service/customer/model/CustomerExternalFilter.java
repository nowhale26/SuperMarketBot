package com.github.supermarket.service.customer.model;

import com.github.supermarket.enums.ExternalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode()
@SuperBuilder
public class CustomerExternalFilter {
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
}
