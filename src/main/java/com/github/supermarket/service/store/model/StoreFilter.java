package com.github.supermarket.service.store.model;

import com.github.supermarket.common.domain.PageFilter;
import com.github.supermarket.common.domain.Pagination;
import com.github.supermarket.common.domain.Sort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class StoreFilter extends PageFilter {
    /**
     * Идентификатор магазина
     */
    private Long storeId;

    /**
     * Признак, что магазин включен(активен)
     */
    private Boolean telegramIsActive;

    /**
     * @param telegramIsActive - true магазин включен, false - магазин выключен
     * @param storeId          - Идентификатор магазина
     * @param pagination       - Пагинация
     * @param sorts            - Сортировка
     */
    public StoreFilter(Boolean telegramIsActive, Long storeId, Pagination pagination, List<Sort> sorts) {
        super(pagination, sorts);
        this.telegramIsActive = telegramIsActive;
        this.storeId = storeId;
    }
}
