package com.github.supermarket.service.store.dao;

import com.github.supermarket.common.dao.GenericJpaDao;
import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.domain.PageControl;
import com.github.supermarket.common.domain.Sort;
import com.github.supermarket.entity.CatalogEntity;
import com.github.supermarket.entity.StoreEntity;
import com.github.supermarket.service.store.model.StoreFilter;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.github.supermarket.entity.QStoreEntity.storeEntity;

@Repository
public class StoreDao extends GenericJpaDao<StoreEntity, Long> {
    private static final Map<String, Path> SORT_DESCRIPTION = ImmutableMap.<String, Path>builder().put("id", storeEntity.id).put("name", storeEntity.name).put("telegramIsActive", storeEntity.telegramIsActive).build();
    private static final List<Sort> DEFAULT_ORDER = List.of(new Sort("id", true));

    private final JPAQueryFactory queryFactory;

    public StoreDao(EntityManager entityManager, JPAQueryFactory queryFactory) {
        super(entityManager);
        this.queryFactory = queryFactory;
    }

    public ListResponse<StoreEntity> list(StoreFilter storeFilter) {
        BooleanExpression where = null;
        if (storeFilter.getTelegramIsActive() != null) {
            var telegramIsActive = storeEntity.telegramIsActive.eq(storeFilter.getTelegramIsActive());
            where = where != null ? where.and(telegramIsActive) : telegramIsActive;
        }
        if (storeFilter.getStoreId() != null) {
            var storeExpression = storeEntity.id.eq(storeFilter.getStoreId());
            where = where != null ? where.and(storeExpression) : storeExpression;
        }

        var storeJPAQuery = queryFactory.selectFrom(storeEntity);
        if (where != null) {
            storeJPAQuery.where(where);
        }

        return new PageControl(storeFilter.getPagination(),
                CollectionUtils.isEmpty(storeFilter.getSorts()) ? DEFAULT_ORDER : storeFilter.getSorts(),
                SORT_DESCRIPTION).execute(storeJPAQuery);
    }
}
