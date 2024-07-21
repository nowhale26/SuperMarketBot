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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Data
@Table(name = "CATALOG")
public class CatalogEntity implements PrimaryKey<Long> {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, columnDefinition = "Уникальный идентификатор")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID", nullable = false, columnDefinition = "Магазин")
    private StoreEntity store;

    @Column(name = "NAME", nullable = false, length = 250, columnDefinition = "Название каталога")
    private String name;

    @Column(name = "SYSNAME", nullable = false, length = 32, columnDefinition = "Системное название каталога")
    private String sysname;

    @Column(name = "IMAGE_URL", nullable = false, length = 1000, columnDefinition = "Путь к изображению")
    private String imageUrl;

    @Column(name = "DISPLAY_ORDER", nullable = false, columnDefinition = "Порядок отображения")
    private Long displayOrder;

    @OneToMany(mappedBy = "catalog", fetch = FetchType.LAZY)
    Set<CatalogProductEntity> catalogProducts;
}
