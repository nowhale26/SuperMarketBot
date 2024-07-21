package com.github.supermarket.service.storecart.mapper;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.service.GenericMapper;
import com.github.supermarket.entity.ProductDetailEntity;
import com.github.supermarket.entity.ProductEntity;
import com.github.supermarket.entity.StoreCartEntity;
import com.github.supermarket.service.product.mapper.ProductDetailMapper;
import com.github.supermarket.service.product.mapper.ProductMapper;
import com.github.supermarket.service.product.model.Product;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.querydsl.core.Tuple;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class StoreCartMapper implements GenericMapper<StoreCart, StoreCartEntity> {
    @Autowired
    private ProductDetailMapper productDetailMapper;
//    @Autowired
//    private ProductMapper productMapper;

    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "customerId", source = "customer.id")
    public abstract StoreCart map(StoreCartEntity store);

    @Mapping(target = "store.id", source = "storeId")
    @Mapping(target = "customer.id", source = "customerId")
    public abstract StoreCartEntity map(StoreCart store);

//    public abstract ListResponse<StoreCart> map(ListResponse<StoreCartEntity> listResponse);

    public abstract ListResponse<StoreCart> map(ListResponse<Tuple> listResponse);
//
    public StoreCart map(Tuple tuple) {
        ProductEntity productEntity = tuple.get(0, ProductEntity.class);
        ProductDetailEntity productDetailEntity = tuple.get(1, ProductDetailEntity.class);
        StoreCartEntity storeCartEntity =  tuple.get(2, StoreCartEntity.class);

        StoreCart storeCart = map(storeCartEntity);
        storeCart.setProductDetail(productDetailMapper.map(productDetailEntity));

        return storeCart;
    }

}
