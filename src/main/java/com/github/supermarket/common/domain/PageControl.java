package com.github.supermarket.common.domain;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class PageControl {
    private final Optional<Pagination> pagination;
    private final List<Sort> sorts;
    private final Map<String, Path> sortDescription;

    public PageControl(Pagination pagination) {
        this(pagination, null, null);
    }

    public PageControl(Pagination pagination, List<Sort> sorts, Map<String, Path> sortDescription) {
        this.pagination = Optional.ofNullable(pagination);
        this.sorts = sorts != null ? sorts : Collections.EMPTY_LIST;
        this.sortDescription = sortDescription != null ? sortDescription : Collections.EMPTY_MAP;
    }

    public <T> JPAQuery<T> updatePage(JPAQuery<T> jpaQuery) {
        pagination.map(Pagination::getOffset).map(offset -> jpaQuery.offset(offset));
        pagination.map(Pagination::getCount).map(count -> jpaQuery.limit(count + 1));

        OrderSpecifier[] orderSpecifiers = sorts.stream().map(sort ->
            Optional.ofNullable(sortDescription.get(sort.getField()))
                    .map(path -> new OrderSpecifier(Boolean.FALSE.equals(sort.getAscending()) ? Order.DESC : Order.ASC, path))
                    .orElseThrow(() -> new NoSuchElementException("Поле '" + sort.getField() + " ' не найдено"))
        ).toArray(size -> new OrderSpecifier[size]);
        jpaQuery.orderBy(orderSpecifiers);

        return jpaQuery;
    }

    public Pagination createResponsePagination(long resultCount) {
        long offset = pagination.map(Pagination::getOffset).orElse(0L);
        boolean hasNextPage = pagination.map(Pagination::getCount).map(count -> resultCount >  count).orElse(false);
        long count = hasNextPage ? pagination.map(Pagination::getCount).orElse(resultCount) : resultCount;
        return new Pagination(offset, count, hasNextPage);
    }

    public long getLimit() {
        return pagination.map(Pagination::getCount).orElse(Long.MAX_VALUE);
    }

    public <T> ListResponse<T> execute(JPAQuery<T> jpaQuery) {
        List<T> result = updatePage(jpaQuery).fetch();
        Pagination pagination = createResponsePagination(result.size());
        List<T> items = result.stream().limit(getLimit()).collect(Collectors.toList());
        return new ListResponse<T>(pagination, items);
    }
}
