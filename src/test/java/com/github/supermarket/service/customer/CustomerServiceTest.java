package com.github.supermarket.service.customer;

import com.github.supermarket.BaseTest;
import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.enums.ExternalType;
import com.github.supermarket.service.customer.model.Customer;
import com.github.supermarket.service.customer.model.CustomerFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerServiceTest extends BaseTest {
    @Autowired
    private CustomerService customerService;

    @Test
    void getByParams() {
        CustomerFilter storeCartFilter = new CustomerFilter(null, 1L, null, null);
        ListResponse<Customer> catalogs = customerService.getByParams(storeCartFilter);
//        Assertions.assertThat(catalogs.getItems()).isEmpty();
    }

    @Test
    void create() {
        Customer customer = customerService.create(new Customer(null, 1L, ExternalType.TELEGRAM, "111", "test", null, null, null, null));
        Assertions.assertThat(customer).isNotNull();

        customer.setName("Иванов");
        Customer update = customerService.update(customer);
        Assertions.assertThat(update).isNotNull();
        Assertions.assertThat(update.getName()).isEqualTo("Иванов");

        Customer customer1 = customerService.get(update.getId());
        Assertions.assertThat(customer1).isNotNull();
        Assertions.assertThat(customer1.getName()).isEqualTo("Иванов");
    }

//    @Test
//    void update() {
//        Customer customer = customerService.create(new Customer(null, 1L, ExternalType.TELEGRAM, "111", "test", null, null, null, null));
//        Assertions.assertThat(customer).isNotNull();
//    }

}
