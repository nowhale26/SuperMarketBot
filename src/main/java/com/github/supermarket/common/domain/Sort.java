package com.github.supermarket.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Sort {
    /**
     * Название поля
     */
    private String field;

    /**
     * Порядок сортировки
     */
    private Boolean ascending = true;
}
