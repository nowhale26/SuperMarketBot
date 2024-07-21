package com.github.supermarket.service.product;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.service.GenericReadService;
import com.github.supermarket.entity.ProductEntity;
import com.github.supermarket.entity.QProductEntity;
import com.github.supermarket.service.product.dao.ProductDao;
import com.github.supermarket.service.product.mapper.ProductMapper;
import com.github.supermarket.service.product.model.Product;
import com.github.supermarket.service.product.model.ProductFilter;
import com.github.supermarket.service.product.model.ProductView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 *  Продукты (товары) в магазине
 */
@Validated
@Service
@Transactional
public class ProductService extends GenericReadService<Product, ProductEntity, Long> {
    private final ProductDao productDao;
    private final ProductMapper productMapper;

    public ProductService(ProductDao productDao, ProductMapper productMapper) {
        super(productDao, productMapper);
        this.productDao = productDao;
        this.productMapper = productMapper;
    }

    /**
     * Список товаров в магазине
     *
     * @param productFilter
     * @return
     */
    public ListResponse<ProductView> getByParams(@Valid ProductFilter productFilter) {
        return productMapper.map(productDao.list(productFilter));
    }
}
