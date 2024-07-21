package com.github.supermarket.service.storecart.model;

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
import javax.validation.constraints.Null;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class StoreCartFilter extends PageFilter {
    /**
     * Идентификатор магазина
     */
    @NotNull
    private Long storeId;

    /**
     * Идентификатор клиента
     */
    private Long customerId;

    /**
     * Тип внешней системы
     */
    private ExternalType externalType;

    /**
     * Идентификатор покупателя во внешней системе
     */
    private String externalId;

    /**
     * Идентификатор детальной информации о продукте
     */
    private Long productDetailId;

    /**
     * @param storeId          - Идентификатор магазина
     * @param customerId       - Идентификатор клиента
     * @param pagination       - Пагинация
     * @param sorts            - Сортировка
     */
    public StoreCartFilter(Long storeId, Long customerId, ExternalType externalType, String externalId, Long productDetailId, Pagination pagination, List<Sort> sorts) {
        super(pagination, sorts);
        this.storeId = storeId;
        this.customerId = customerId;
        this.externalType = externalType;
        this.externalId = externalId;
        this.productDetailId = productDetailId;
    }
}
