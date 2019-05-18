package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.GenericRepositoryHiber;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Repository
public abstract class GenericRepositoryHiberImpl<I, T> implements GenericRepositoryHiber<I, T> {
    protected Class<T> entityClass;
    @PersistenceContext
    protected EntityManager entityManager;

    public GenericRepositoryHiberImpl() {
        ParameterizedType genericSuperClass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperClass.getActualTypeArguments()[1];
    }

    @Override
    public void persist(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void merge(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public T findById(I id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        String query = "from " + entityClass.getName();
        Query q = entityManager.createQuery(query);
        return q.getResultList();
    }

    @Override
    public Long getAmountOfEntity() {
        String query = "select count(e) from " + entityClass.getName() + " e";
        Query q = entityManager.createQuery(query, Long.class);
        return (Long) q.getSingleResult();
    }

    @Override
    public List<T> getEntities(Integer offset, Integer amount) {
        String query = "from " + entityClass.getName() + " e";
        Query q = entityManager.createQuery(query);
        q.setFirstResult(offset);
        q.setMaxResults(amount);
        return q.getResultList();
    }
}