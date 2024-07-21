package com.github.supermarket.common.dao;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface GenericDao<Entity, ID> {
    Entity create(Entity entity);

    Optional<Entity> findById(ID id, LockModeType type);

    Optional<Entity> findById(ID id);

//    T get(PK id);
    Entity update(Entity entity);
    void delete(ID id);
    void flush();
//    Class<T> getEntityClass();

//    <P, R> Page<R> browse(Request<P> request, Class<R> resultClass);
//    <P, R> List<R> list(Request<P> request, Class<R> resultClass);

//    <P, R> Page<R> browse(P parameters, Class<R> resultClass, Integer pageNumber, Integer pageSize, List<Sort> sortFields);
//    <P, R> List<R> list(P parameters, Class<R> resultClass, Integer pageNumber, Integer pageSize, List<Sort> sortFields);
}
