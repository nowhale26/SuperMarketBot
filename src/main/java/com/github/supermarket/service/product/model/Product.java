package com.github.supermarket.service.product.model;

import com.github.supermarket.service.storecart.model.StoreCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Product implements Serializable {
    private Long id;
    private Long storeId;
    private String name;
    private String description;
    private String imageUrl;
    private Long displayOrder;
//    private ProductDetail productDetail;
//    private StoreCart storeCart;
}
