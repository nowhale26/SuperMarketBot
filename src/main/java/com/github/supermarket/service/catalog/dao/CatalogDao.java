package com.github.supermarket.service.catalog.dao;

import com.github.supermarket.common.dao.GenericJpaDao;
import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.domain.PageControl;
import com.github.supermarket.common.domain.Sort;
import com.github.supermarket.entity.CatalogEntity;
import com.github.supermarket.entity.CustomerEntity;
import com.github.supermarket.service.catalog.model.CatalogFilter;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.github.supermarket.entity.QCatalogEntity.catalogEntity;

@Repository
public class CatalogDao extends GenericJpaDao<CatalogEntity, Long> {
    private static final Map<String, Path> SORT_DESCRIPTION = ImmutableMap.<String, Path>builder()
            .put("name", catalogEntity.name)
            .put("displayorder", catalogEntity.displayOrder)
            .build();

    private static final List<Sort> DEFAULT_ORDER = List.of(new Sort("displayorder", true), new Sort("name", true));

    private final JPAQueryFactory queryFactory;

    public CatalogDao(EntityManager entityManager, JPAQueryFactory queryFactory) {
        super(entityManager);
        this.queryFactory = queryFactory;
    }

    public ListResponse<CatalogEntity> list(CatalogFilter catalogFilter) {
        BooleanExpression where = catalogEntity.store.id.eq(catalogFilter.getStoreId());
        if (StringUtils.isNotEmpty(catalogFilter.getSysname())) {
            where = where.and(catalogEntity.sysname.eq(catalogFilter.getSysname()));
        }
        if (StringUtils.isNotEmpty(catalogFilter.getName())) {
            where = where.and(catalogEntity.name.eq(catalogFilter.getName()));
        }

        PageControl pageControl = new PageControl(
                catalogFilter.getPagination(),
                CollectionUtils.isEmpty(catalogFilter.getSorts()) ? DEFAULT_ORDER : catalogFilter.getSorts(),
                SORT_DESCRIPTION
        );

        return pageControl.execute(
                queryFactory.selectFrom(catalogEntity).where(where)
        );
//        return pageControl.execute(
//                queryFactory.selectFrom(catalog)
//                        .innerJoin(catalog.store, store)
//                        .innerJoin(store.org, org)
//                        .where(org.id.eq(catalogFilter.getOrgId()))
//        );
    }
}
