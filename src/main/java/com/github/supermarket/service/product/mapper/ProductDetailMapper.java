package com.github.supermarket.service.product.mapper;

import com.github.supermarket.entity.ProductDetailEntity;
import com.github.supermarket.entity.ProductEntity;
import com.github.supermarket.service.product.model.Product;
import com.github.supermarket.service.product.model.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductDetailMapper {

    @Mapping(target = "sizeTypeId", source = "sizeType.id")
    ProductDetail map(ProductDetailEntity productDetailEntity);

}
