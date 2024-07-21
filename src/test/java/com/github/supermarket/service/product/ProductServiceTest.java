package com.github.supermarket.service.product;

import com.github.supermarket.BaseTest;
import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.domain.Pagination;
import com.github.supermarket.common.domain.Sort;
import com.github.supermarket.enums.ExternalType;
import com.github.supermarket.service.product.model.Product;
import com.github.supermarket.service.product.model.ProductFilter;
import com.github.supermarket.service.product.model.ProductView;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.List;

public class ProductServiceTest extends BaseTest {
    @Autowired
    private ProductService productService;
    private static Long storeId;
    private static String externalId;

    @BeforeAll
    public static void init() {
        storeId = RandomUtils.nextLong();
        externalId = RandomStringUtils.randomNumeric(10);
    }

    @Test
    public void listProduct() {
        Pagination pagination = new Pagination(0L, 10L);
        ProductFilter productFilter = new ProductFilter(1L, null, null, ExternalType.TELEGRAM, externalId, pagination, null);
        ListResponse<ProductView> products = productService.getByParams(productFilter);
        Assertions.assertThat(products.getItems()).isNotEmpty();

        productFilter.setCatalogId(1L);
        products = productService.getByParams(productFilter);
        Assertions.assertThat(products.getItems()).isNotEmpty();

        productFilter.setSorts(List.of(new Sort("name", null)));
        products = productService.getByParams(productFilter);
        Assertions.assertThat(products.getItems()).isNotEmpty();
    }
}
