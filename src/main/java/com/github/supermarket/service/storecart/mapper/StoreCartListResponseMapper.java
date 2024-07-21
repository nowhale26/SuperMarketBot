package com.github.supermarket.service.storecart.mapper;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.entity.StoreCartEntity;
import com.github.supermarket.service.storecart.model.StoreCart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StoreCartMapper.class})
public interface StoreCartListResponseMapper {

    ListResponse<StoreCart> toFront(ListResponse<StoreCartEntity> listResponse);
}
