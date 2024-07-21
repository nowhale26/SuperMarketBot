package com.github.supermarket.service.customer.dao;

import com.github.supermarket.common.dao.GenericJpaDao;
import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.domain.PageControl;
import com.github.supermarket.common.domain.Sort;
import com.github.supermarket.entity.CustomerEntity;
import com.github.supermarket.service.customer.model.CustomerExternalFilter;
import com.github.supermarket.service.customer.model.CustomerFilter;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.github.supermarket.entity.QCatalogEntity.catalogEntity;
import static com.github.supermarket.entity.QCustomerEntity.customerEntity;

@Repository
public class CustomerDao extends GenericJpaDao<CustomerEntity, Long> {

    private static final Map<String, Path> SORT_DESCRIPTION = ImmutableMap.<String, Path>builder()
            .put("name", customerEntity.name)
            .put("sysname", customerEntity.sysname)
            .build();

    private static final List<Sort> DEFAULT_ORDER = List.of(new Sort("sysname", true));

    private final JPAQueryFactory queryFactory;

    public CustomerDao(EntityManager entityManager, JPAQueryFactory queryFactory) {
        super(entityManager);
        this.queryFactory = queryFactory;
    }

    public ListResponse<CustomerEntity> list(CustomerFilter customerFilter) {
        PageControl pageControl = new PageControl(
                customerFilter.getPagination(),
                CollectionUtils.isEmpty(customerFilter.getSorts()) ? DEFAULT_ORDER : customerFilter.getSorts(),
                SORT_DESCRIPTION
        );

        BooleanExpression where = customerEntity.store.id.eq(customerFilter.getStoreId());
        if (customerFilter.getId() != null) {
            where = where.and(customerEntity.id.eq(customerFilter.getId()));
        }
        if (customerFilter.getExternalType() != null) {
            where = where.and(customerEntity.externalType.eq(customerFilter.getExternalType()));
        }
        if (customerFilter.getExternalId() != null) {
            where = where.and(customerEntity.externalId.eq(customerFilter.getExternalId()));
        }

        return pageControl.execute(
                queryFactory.selectFrom(customerEntity).where(where)
        );
    }

    public CustomerEntity getByExternalId(CustomerExternalFilter customerExternalFilter) {
        return queryFactory.selectFrom(customerEntity)
                .where(customerEntity.store.id.eq(customerExternalFilter.getStoreId())
                        .and(customerEntity.externalType.eq(customerExternalFilter.getExternalType()))
                        .and(customerEntity.externalId.eq(customerExternalFilter.getExternalId())))
                .fetchOne();
    }

}
