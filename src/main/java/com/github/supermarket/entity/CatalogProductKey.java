package com.github.supermarket.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class CatalogProductKey implements Serializable {
    @Column(name = "CATALOG_ID", nullable = false, columnDefinition = "Идентификатор каталога")
    private Long catalogId;

    @Column(name = "PRODUCT_ID", nullable = false, columnDefinition = "Идентификатор продукта")
    private Long productId;
}
