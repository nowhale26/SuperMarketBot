package com.github.supermarket.service.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Catalog {
    /**
     * Уникальный идентификатор
     */
    private Long id;

    /**
     * Уникальный идентификатор магазина
     */
    private Long storeId;

    /**
     * Название каталога
     */
    private String name;

    /**
     *  Системное название каталога
     */
    private String sysname;

    /**
     * Путь к изображению
     */
    private String imageUrl;

    /**
     * Порядок отображения
     */
    private Long displayOrder;
}
