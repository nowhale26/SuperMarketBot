package com.github.supermarket.service.store;

import com.github.supermarket.BaseTest;
import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.domain.Pagination;
import com.github.supermarket.service.store.model.Store;
import com.github.supermarket.service.store.model.StoreFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StoreServiceTest extends BaseTest {
    @Autowired
    private StoreService storeService;

    @Test
    public void listCatalog() {
        Pagination pagination = new Pagination(0L, 10L);
        StoreFilter storeFilter = new StoreFilter(null, null, pagination, null);
        ListResponse<Store> catalogs = storeService.getByParams(storeFilter);
        Assertions.assertThat(catalogs.getItems()).isNotEmpty();
    }
}
