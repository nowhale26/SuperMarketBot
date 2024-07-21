package com.github.supermarket.service.catalog.mapper;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.service.GenericMapper;
import com.github.supermarket.entity.CatalogEntity;
import com.github.supermarket.entity.CustomerEntity;
import com.github.supermarket.service.catalog.model.Catalog;
import com.github.supermarket.service.customer.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CatalogMapper extends GenericMapper<Catalog, CatalogEntity> {

    @Mapping(target = "storeId", source = "store.id")
    Catalog map(CatalogEntity catalogEntity);

    @Mapping(target = "store.id", source = "storeId")
    CatalogEntity map(Catalog catalog);

    ListResponse<Catalog> map(ListResponse<CatalogEntity> listResponse);
}
