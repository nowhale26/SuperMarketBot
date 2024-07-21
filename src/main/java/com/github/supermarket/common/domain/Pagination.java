package com.github.supermarket.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 *  Пагинация
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Pagination {
    /**
     * Смещение от начала
     */
    private Long offset;

    /**
     * Количество возвращаемых записей
     */
    private Long count;

    /**
     * Признак наличия следующей страницы
     */
    private Boolean hasNextPage;

    public Pagination(Long offset, Long count) {
        this.offset = offset;
        this.count = count;
    }
}
