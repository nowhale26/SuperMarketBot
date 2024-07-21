package com.github.supermarket.service.catalog;

import com.github.supermarket.BaseTest;
import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.domain.Pagination;
import com.github.supermarket.service.catalog.model.Catalog;
import com.github.supermarket.service.catalog.model.CatalogFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CatalogServiceTest extends BaseTest {
    @Autowired
    private CatalogService catalogService;

    @Test
    public void listCatalog() {
        Pagination pagination = new Pagination(0L, 10L);
        CatalogFilter catalogFilter = CatalogFilter.builder().storeId(1L).pagination(pagination).build();
        ListResponse<Catalog> catalogs = catalogService.getByParams(catalogFilter);
        Assertions.assertThat(catalogs.getItems()).isNotEmpty();
    }
}
