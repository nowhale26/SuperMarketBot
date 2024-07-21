package com.github.supermarket.common.dao;

import com.github.supermarket.common.domain.PrimaryKey;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class GenericJpaDao<Entity extends PrimaryKey<ID>, ID> implements GenericDao<Entity, ID> {
    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null.";
    public static final String ENTITY_MUST_NOT_BE_NULL = "Entity must not be null.";

    protected final Class<Entity> entityClass;
    protected final EntityManager entityManager;

    public GenericJpaDao(EntityManager entityManager) {
        this.entityManager = entityManager;

        // чтобы aspectj корректно отрабатывал, делаем цикл
        Type genericSuperclass;
        Class<?> parametrizedClass = getClass();
        do {
            genericSuperclass = parametrizedClass.getGenericSuperclass();
            if (genericSuperclass instanceof Class) {
                parametrizedClass = (Class<?>) genericSuperclass;
            }
        } while (genericSuperclass != null && !(genericSuperclass instanceof ParameterizedType));

        if (genericSuperclass instanceof ParameterizedType) {
            this.entityClass = (Class<Entity>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException(String.format("%s does not contain generic Entity and ID parameters", getClass().getName()));
        }
    }

    @Override
    public Entity create(Entity entity) {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

        entityManager.persist(entity);
        return entity;
    }


//    Optional<T> findById(ID id);

    @Override
    public Optional<Entity> findById(ID id, LockModeType type) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

//        Class<T> domainType = getDomainClass();
//
//        if (metadata == null) {
//            return Optional.ofNullable(em.find(domainType, id));
//        }
//
//        LockModeType type = metadata.getLockModeType();
//
//        Map<String, Object> hints = new HashMap<>();
//        getQueryHints().withFetchGraphs(em).forEach(hints::put);

        return Optional.ofNullable(type == null ? entityManager.find(entityClass, id) : entityManager.find(entityClass, id, type));
    }

    @Override
    public Optional<Entity> findById(ID id) {
        return findById(id, null);
    }

//    @Override
//    public T get(PK id) {
//        return entityManager.find(entityClass, id);
//    }

    @Override
    public Entity update(Entity entity) {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);
//        if (entityManager.find(entityClass, t.getId()) == null) {
//            throw new EntityExistsException("Not found " + entityClass + " by id: " + t.getId());
//        }
        return entityManager.merge(entity);
    }

    @Override
    public void delete(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Entity entity = this.entityManager.find(entityClass, id);
        this.entityManager.remove(entity);
    }

    public void delete(Entity entity) {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

//        T t = this.entityManager.find(entityClass, id);
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
//        this.entityManager.remove(t);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

//    @Override
//    public Class<T> getEntityClass() {
//        return entityClass;
//    }

//    public EntityManager getEntityManager() {
//        return entityManager;
//    }
//
//    @PersistenceContext
//    public void setEntityManager(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }


}
