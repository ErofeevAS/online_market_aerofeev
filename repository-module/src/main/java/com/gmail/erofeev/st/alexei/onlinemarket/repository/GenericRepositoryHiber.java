package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import java.util.List;

public interface GenericRepositoryHiber<I, T> {
    void persist(T entity);

    void merge(T entity);

    void remove(T entity);

    T findById(I id);

    List<T> findAll();

    Long getAmountOfEntity();

    List<T> getEntities(Integer offset, Integer amount);

}
