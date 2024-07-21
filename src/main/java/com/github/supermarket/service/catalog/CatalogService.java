package com.github.supermarket.service.catalog;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.service.GenericReadService;
import com.github.supermarket.entity.CatalogEntity;
import com.github.supermarket.service.catalog.dao.CatalogDao;
import com.github.supermarket.service.catalog.mapper.CatalogMapper;
import com.github.supermarket.service.catalog.model.Catalog;
import com.github.supermarket.service.catalog.model.CatalogFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 *  Каталог товаров
 */
@Validated
@Service
@Transactional
public class CatalogService extends GenericReadService<Catalog, CatalogEntity, Long> {
    private final CatalogDao catalogDao;
    private final CatalogMapper catalogMapper;

    public CatalogService(CatalogDao catalogDao, CatalogMapper catalogMapper) {
        super(catalogDao, catalogMapper);
        this.catalogDao = catalogDao;
        this.catalogMapper = catalogMapper;
    }

    /**
     * Список элементов каталога в магазине
     *
     * @param catalogFilter
     * @return
     */
    public ListResponse<Catalog> getByParams(@Valid CatalogFilter catalogFilter) {
        return catalogMapper.map(catalogDao.list(catalogFilter));
    }
}
