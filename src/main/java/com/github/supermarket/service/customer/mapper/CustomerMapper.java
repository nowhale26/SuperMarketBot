package com.github.supermarket.service.customer.mapper;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.service.GenericMapper;
import com.github.supermarket.entity.CustomerEntity;
import com.github.supermarket.service.customer.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends GenericMapper<Customer, CustomerEntity> {

    @Mapping(source = "store.id", target = "storeId")
    Customer map(CustomerEntity catalogEntity);

    @Mapping(source = "storeId", target = "store.id")
    CustomerEntity map(Customer catalog);

    ListResponse<Customer> map(ListResponse<com.github.supermarket.entity.CustomerEntity> listResponse);
}
