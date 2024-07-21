package com.github.supermarket.entity;

import com.github.supermarket.common.domain.PrimaryKey;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "CATALOG_PRODUCT")
public class CatalogProductEntity implements PrimaryKey<CatalogProductKey> {
    @EmbeddedId
    private CatalogProductKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("catalogId")
    @JoinColumn(name = "catalog_id")
    private CatalogEntity catalog;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
