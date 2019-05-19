package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import java.sql.Connection;
import java.util.List;

public interface GenericRepository<I, T> {
    Connection getConnection();

    void persist(T entity);

    void merge(T entity);

    void remove(T entity);

    T findById(I id);

    List<T> findAll();

    Integer getAmountOfEntity();

    List<T> getEntities(Integer offset, Integer amount);
}