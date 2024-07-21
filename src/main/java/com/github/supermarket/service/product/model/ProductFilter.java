package com.github.supermarket.service.product.model;

import com.github.supermarket.common.domain.PageFilter;
import com.github.supermarket.common.domain.Pagination;
import com.github.supermarket.common.domain.Sort;
import com.github.supermarket.enums.ExternalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@AllArgsConstructor()
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ProductFilter extends PageFilter {
    /**
     * Идентификатор магазина
     */
    @NotNull
    private Long storeId;

    /**
     * Идентификатор каталога
     */
    private Long catalogId;

    /**
     * Системное имя каталога
     */
    private String catalogSysname;

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

    public ProductFilter(Long storeId, Long catalogId, String catalogSysname, ExternalType externalType, String externalId, Pagination pagination, List<Sort> sorts) {
        super(pagination, sorts);
        this.storeId = storeId;
        this.catalogId = catalogId;
        this.catalogSysname = catalogSysname;
        this.externalType = externalType;
        this.externalId = externalId;
    }
}
