package com.github.supermarket.service.storecart.dao;

import com.github.supermarket.common.dao.GenericJpaDao;
import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.domain.PageControl;
import com.github.supermarket.common.domain.Sort;
import com.github.supermarket.entity.StoreCartEntity;
import com.github.supermarket.service.storecart.model.StoreCartFilter;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.github.supermarket.entity.QCustomerEntity.customerEntity;
import static com.github.supermarket.entity.QProductDetailEntity.productDetailEntity;
import static com.github.supermarket.entity.QProductEntity.productEntity;
import static com.github.supermarket.entity.QStoreCartEntity.storeCartEntity;

@Repository
public class StoreCartDao extends GenericJpaDao<StoreCartEntity, Long> {
    private static final Map<String, Path> SORT_DESCRIPTION = ImmutableMap.<String, Path>builder()
            .put(storeCartEntity.id.getMetadata().getName(), storeCartEntity.id)
            .put(storeCartEntity.quantity.getMetadata().getName(), storeCartEntity.quantity)
            .put(storeCartEntity.displayOrder.getMetadata().getName(), storeCartEntity.displayOrder)
            .build();
    private static final List<Sort> DEFAULT_ORDER = List.of(new Sort(storeCartEntity.displayOrder.getMetadata().getName(), true));

    private final JPAQueryFactory queryFactory;

    public StoreCartDao(EntityManager entityManager, JPAQueryFactory queryFactory) {
        super(entityManager);
        this.queryFactory = queryFactory;
    }

    public ListResponse<Tuple> list(StoreCartFilter storeCartFilter) {
        BooleanExpression where = storeCartEntity.store.id.eq(storeCartFilter.getStoreId());
        if (storeCartFilter.getCustomerId() != null) {
            where = where.and(storeCartEntity.customer.id.eq(storeCartFilter.getCustomerId()));
        }
        if (storeCartFilter.getExternalType() != null) {
            where = where.and(customerEntity.externalType.eq(storeCartFilter.getExternalType()));
        }
        if (storeCartFilter.getExternalId() != null) {
            where = where.and(customerEntity.externalId.eq(storeCartFilter.getExternalId()));
        }
        if (storeCartFilter.getProductDetailId() != null) {
            where = where.and(storeCartEntity.productDetail.id.eq(storeCartFilter.getProductDetailId()));
        }

        var storeJPAQuery = queryFactory
                .select(productEntity, productDetailEntity, storeCartEntity)
                .from(storeCartEntity)
                .innerJoin(storeCartEntity.customer, customerEntity)
                .innerJoin(storeCartEntity.productDetail, productDetailEntity)
                .innerJoin(productDetailEntity.product, productEntity)
                .where(where);

        return new PageControl(
                storeCartFilter.getPagination(),
                CollectionUtils.isEmpty(storeCartFilter.getSorts()) ? DEFAULT_ORDER : storeCartFilter.getSorts(), SORT_DESCRIPTION
        ).execute(storeJPAQuery);
    }

}
