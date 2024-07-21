package com.github.supermarket.service.storecart;

import com.github.supermarket.BaseTest;
import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.enums.ExternalType;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.github.supermarket.service.storecart.model.StoreCartFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StoreCartServiceTest extends BaseTest {
    @Autowired
    private StoreCartService storeCartService;

    @Test
    void getByParams() {
//        Pagination pagination = new Pagination(0L, 10L);
        StoreCartFilter storeCartFilter = new StoreCartFilter(1L, null, ExternalType.TELEGRAM, "111", null);
        ListResponse<StoreCart> storeCarts = storeCartService.getByParams(storeCartFilter);
        Assertions.assertThat(storeCarts.getItems()).isEmpty();
    }

    @Test
    void getProductByParams() {
    }

    @Test
    void create() {
//        storeCartService.add(new StoreCart(null, 1L, ))
    }

    @Test
    void update() {
    }
}