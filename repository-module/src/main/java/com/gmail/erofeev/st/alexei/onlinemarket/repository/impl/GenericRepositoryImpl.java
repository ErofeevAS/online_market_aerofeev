package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.GenericRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {
    protected Class<T> entityClass;
    @PersistenceContext
    protected EntityManager entityManager;

    public GenericRepositoryImpl() {
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
        String hql = "from " + entityClass.getName();
        Query query = entityManager.createQuery(hql);
        return query.getResultList();
    }

    @Override
    public Integer getAmountOfEntity() {
        String hql = "select count(e) from " + entityClass.getName() + " e";
        Query query = entityManager.createQuery(hql);
        return ((Number) query.getSingleResult()).intValue();
    }

    @Override
    public List<T> getEntities(Integer offset, Integer amount) {
        String hql = "from " + entityClass.getName() + " e";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(offset);
        query.setMaxResults(amount);
        return query.getResultList();
    }
}
