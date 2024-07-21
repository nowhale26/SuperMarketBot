package com.github.supermarket.entity;

import com.github.supermarket.common.domain.PrimaryKey;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "SIZE_TYPE")
public class SizeTypeEntity implements PrimaryKey<Long> {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, columnDefinition = "Уникальный идентификатор")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID", nullable = false, columnDefinition = "Магазин")
    private StoreEntity store;

    @Column(name = "NAME", nullable = false, length = 250, columnDefinition = "Название размера")
    private String name;

    @Column(name = "DISPLAY_ORDER", nullable = false, columnDefinition = "Порядок отображения")
    private Long displayOrder;
}
