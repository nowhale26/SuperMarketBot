package com.github.supermarket.service.customer;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.service.GenericService;
import com.github.supermarket.entity.CustomerEntity;
import com.github.supermarket.service.customer.dao.CustomerDao;
import com.github.supermarket.service.customer.mapper.CustomerMapper;
import com.github.supermarket.service.customer.model.Customer;
import com.github.supermarket.service.customer.model.CustomerExternalFilter;
import com.github.supermarket.service.customer.model.CustomerFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 *  Пользователи
 */
@Validated
@Service
@Transactional
public class CustomerService extends GenericService<Customer, CustomerEntity, Long> {
    private final CustomerDao customerDao;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerDao customerDao, CustomerMapper customerMapper) {
        super(customerDao, customerMapper);
        this.customerDao = customerDao;
        this.customerMapper = customerMapper;
    }

    /**
     * Список элементов каталога в магазине
     *
     * @param customerFilter
     * @return
     */
    public ListResponse<Customer> getByParams(@Valid CustomerFilter customerFilter) {
        return customerMapper.map(customerDao.list(customerFilter));
    }

    public Customer getByExternalId(@Valid CustomerExternalFilter customerExternalFilter) {
        return customerMapper.map(customerDao.getByExternalId(customerExternalFilter));
    }

    public Customer createOrUpdate(@Valid Customer customer) {
        Customer existCustomer = getByExternalId(new CustomerExternalFilter(customer.getStoreId(), customer.getExternalType(), customer.getExternalId()));
        if (existCustomer == null) {
            return create(customer);
        } else {
            if (!StringUtils.equals(existCustomer.getName(), customer.getName())
                    || !StringUtils.equals(existCustomer.getAddress(), customer.getAddress())
                    || !StringUtils.equals(existCustomer.getSysname(), customer.getSysname())
                    || !StringUtils.equals(existCustomer.getPhone(), customer.getPhone())
                    || !StringUtils.equals(existCustomer.getEmail(), customer.getEmail())
            ) {
                customer.setId(existCustomer.getId());
                return update(customer);
            }
            return existCustomer;
        }
    }

}
