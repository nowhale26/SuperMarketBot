package com.github.supermarket.service.store.mapper;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.service.GenericMapper;
import com.github.supermarket.entity.StoreEntity;
import com.github.supermarket.service.store.model.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoreMapper extends GenericMapper<Store, StoreEntity> {

    @Mapping(target = "orgId", source = "org.id")
    Store map(StoreEntity storeEntity);

    @Mapping(target = "org.id", source = "orgId")
    StoreEntity map(Store storeEntity);

    ListResponse<Store> map(ListResponse<StoreEntity> listResponse);
}
