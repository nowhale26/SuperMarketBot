package com.github.supermarket.service.store;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.service.GenericReadService;
import com.github.supermarket.entity.StoreEntity;
import com.github.supermarket.service.store.dao.StoreDao;
import com.github.supermarket.service.store.mapper.StoreMapper;
import com.github.supermarket.service.store.model.Store;
import com.github.supermarket.service.store.model.StoreFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * Магазин
 */
@Validated
@Service
@Transactional
public class StoreService extends GenericReadService<Store, StoreEntity, Long> {
    private final StoreDao storeDao;
    private final StoreMapper storeMapper;

    public StoreService(StoreDao storeDao, StoreMapper storeMapper) {
        super(storeDao, storeMapper);
        this.storeDao = storeDao;
        this.storeMapper = storeMapper;
    }

    /**
     * Список магазинов
     *
     * @param storeFilter
     * @return
     */
    public ListResponse<Store> getByParams(@Valid StoreFilter storeFilter) {
        return storeMapper.map(storeDao.list(storeFilter));
    }
}
