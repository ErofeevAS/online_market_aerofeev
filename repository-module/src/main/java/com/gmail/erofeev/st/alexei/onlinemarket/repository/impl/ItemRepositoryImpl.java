package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ItemRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {
    @Override
    public List<Item> findItems(Integer offset, Integer amount, boolean showDeleted) {
        String hql;
        if (showDeleted) {
            hql = "select i from Item i ORDER BY i.name asc ";
        } else {
            hql = "select i from Item i where i.deleted = false ORDER BY i.name asc  ";
        }
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(offset);
        query.setMaxResults(amount);
        return query.getResultList();
    }
}