package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.GenericRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {
    private static final Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);
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
        String query = "from " + entityClass.getName();
        Query q = entityManager.createQuery(query);
        return q.getResultList();
    }

    @Override
    public Integer getAmountOfEntity() {
        String query = "select count(e) from " + entityClass.getName() + " e";
        Query q = entityManager.createQuery(query);
        return ((Number) q.getSingleResult()).intValue();
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
