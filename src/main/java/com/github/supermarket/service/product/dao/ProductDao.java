package com.github.supermarket.service.product.dao;

import com.github.supermarket.common.dao.GenericJpaDao;
import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.domain.PageControl;
import com.github.supermarket.common.domain.Sort;
import com.github.supermarket.entity.CatalogEntity;
import com.github.supermarket.entity.ProductEntity;
import com.github.supermarket.service.product.model.ProductFilter;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.github.supermarket.entity.QCatalogProductEntity.catalogProductEntity;
import static com.github.supermarket.entity.QCustomerEntity.customerEntity;
import static com.github.supermarket.entity.QProductEntity.productEntity;
import static com.github.supermarket.entity.QProductDetailEntity.productDetailEntity;
import static com.github.supermarket.entity.QStoreCartEntity.storeCartEntity;
import static org.springframework.util.CollectionUtils.isEmpty;

@Repository
public class ProductDao extends GenericJpaDao<ProductEntity, Long> {
    private static final Map<String, Path> SORT_DESCRIPTION = ImmutableMap.<String, Path>builder()
            .put("id", productEntity.id)
            .put("name", productEntity.name)
            .put("displayorder", productEntity.displayOrder)
            .build();
    private static final List<Sort> DEFAULT_ORDER = List.of(new Sort("displayorder", true), new Sort("name", true));

    private final JPAQueryFactory queryFactory;

    public ProductDao(EntityManager entityManager, JPAQueryFactory queryFactory) {
        super(entityManager);
        this.queryFactory = queryFactory;
    }

    public ListResponse<Tuple> list(ProductFilter productFilter) {
        JPAQuery<Tuple> jpaQuery = queryFactory
                .select(productEntity, productDetailEntity, storeCartEntity)
                .from(productEntity)
                .innerJoin(productDetailEntity).on(productDetailEntity.product.id.eq(productEntity.id).and(productDetailEntity.isDefault.eq(true)))
                .leftJoin(storeCartEntity).on(storeCartEntity.productDetail.id.eq(productDetailEntity.id))
                .leftJoin(customerEntity).on(customerEntity.id.eq(storeCartEntity.customer.id)
                        .and(customerEntity.externalType.eq(productFilter.getExternalType()))
                        .and(customerEntity.externalId.eq(productFilter.getExternalId()))
                );

        BooleanExpression where = productEntity.store.id.eq(productFilter.getStoreId());

        if (productFilter.getCatalogId() != null || StringUtils.isNotEmpty(productFilter.getCatalogSysname())) {
            jpaQuery.innerJoin(productEntity.catalogProducts, catalogProductEntity);
            if (productFilter.getCatalogId() != null) {
                where = where.and(catalogProductEntity.catalog.id.eq(productFilter.getCatalogId()));
            }
            if (StringUtils.isNotEmpty(productFilter.getCatalogSysname())) {
                where = where.and(catalogProductEntity.catalog.sysname.eq(productFilter.getCatalogSysname()));
            }
        }

        PageControl pageControl = new PageControl(
                productFilter.getPagination(),
                isEmpty(productFilter.getSorts()) ? DEFAULT_ORDER : productFilter.getSorts(), SORT_DESCRIPTION
        );
        return pageControl.execute(
                jpaQuery.where(where)
        );
    }
}
