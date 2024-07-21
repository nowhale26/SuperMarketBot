package com.github.supermarket.service.product.mapper;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.service.GenericMapper;
import com.github.supermarket.entity.CustomerEntity;
import com.github.supermarket.entity.ProductDetailEntity;
import com.github.supermarket.entity.ProductEntity;
import com.github.supermarket.entity.StoreCartEntity;
import com.github.supermarket.service.customer.model.Customer;
import com.github.supermarket.service.product.model.Product;
import com.github.supermarket.service.product.model.ProductView;
import com.github.supermarket.service.storecart.mapper.StoreCartMapper;
import com.querydsl.core.Tuple;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProductMapper implements GenericMapper<Product, ProductEntity> {
    @Mapping(target = "storeId", source = "store.id")
    public abstract Product map(ProductEntity productEntity);

    public abstract ProductView mapView(Product product);

    @Mapping(target = "store.id", source = "storeId")
    public abstract ProductEntity map(Product product);

    public abstract ListResponse<ProductView> map(ListResponse<Tuple> listResponse);

    public ProductView map(Tuple tuple) {
        ProductEntity productEntity = tuple.get(0, ProductEntity.class);
        ProductDetailEntity productDetailEntity = tuple.get(1, ProductDetailEntity.class);
        StoreCartEntity storeCartEntity =  tuple.get(2, StoreCartEntity.class);

        ProductView productView = mapView(map(productEntity));
        if (productDetailEntity != null) {
            productView.setProductDetailId(productDetailEntity.getId());
            productView.setPrice(productDetailEntity.getPrice());
        }
        if (storeCartEntity != null) {
            productView.setStoreCartQuantity(storeCartEntity.getQuantity());
        }
        return productView;
    }
}
