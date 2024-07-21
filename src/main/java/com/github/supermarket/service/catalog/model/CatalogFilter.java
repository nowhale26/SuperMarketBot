package com.github.supermarket.service.catalog.model;

import com.github.supermarket.common.domain.PageFilter;
import com.github.supermarket.common.domain.Pagination;
import com.github.supermarket.common.domain.Sort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CatalogFilter extends PageFilter {
    /**
     * Идентификатор магазина
     */
    @NotNull
    private Long storeId;

    /**
     *  Название каталога
     */
    private String name;

    /**
     *  Системное название каталога
     */
    private String sysname;

    /**
     *
     * @param storeId - Идентификатор магазина
     * @param pagination - Пагинация
     * @param sorts - Сортировка
     */
    public CatalogFilter(Long storeId, String sysname, Pagination pagination, List<Sort> sorts) {
        super(pagination, sorts);
        this.storeId = storeId;
        this.sysname = sysname;
    }
}
