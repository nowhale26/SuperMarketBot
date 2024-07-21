package com.github.supermarket.service.customer.model;

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
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CustomerFilter extends PageFilter {
    /**
     * Уникальный идентификатор пользователя
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
    private ExternalType externalType;

    /**
     * Идентификатор покупателя во внешней системе
     */
    private String externalId;

    /**
     *
     * @param storeId - Идентификатор магазина
     * @param pagination - Пагинация
     * @param sorts - Сортировка
     */
    public CustomerFilter(Long storeId, Pagination pagination, List<Sort> sorts) {
        super(pagination, sorts);
        this.storeId = storeId;
    }
}
