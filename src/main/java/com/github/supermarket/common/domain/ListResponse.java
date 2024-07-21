package com.github.supermarket.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ListResponse<T> {
    /**
     *  Информация о странице
     */
    private Pagination pagination;

    /**
     * Возвращаемая коллекция данных страницы
     */
    private List<T> items;
}
