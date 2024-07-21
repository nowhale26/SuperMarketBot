package com.github.supermarket.service.store.mapper;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.entity.StoreEntity;
import com.github.supermarket.service.store.model.Store;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StoreMapper.class})
public interface StoreListResponseMapper {

    ListResponse<Store> toFront(ListResponse<StoreEntity> listResponse);
}
