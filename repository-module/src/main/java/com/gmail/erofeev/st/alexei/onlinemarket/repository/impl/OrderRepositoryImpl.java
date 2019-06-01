package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.OrderRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.embedded.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {

    @Override
    public Order findByUUID(String uniqueNumber) {
        String hql = "select o from Order o where o.uniqueNumber = :uniqueNumber";
        Query query = entityManager.createQuery(hql);
        query.setParameter("uniqueNumber", uniqueNumber);
        return (Order) query.getSingleResult();
    }

    @Override
    public List<Order> getOrders(int offset, int amount, Long userId) {
        String hql;
        Query query;
        if (userId == null) {
            hql = "select o from Order o ";
            query = entityManager.createQuery(hql);
        } else {
            hql = "select o from Order o where o.user.id = :userId";
            query = entityManager.createQuery(hql);
            query.setParameter("userId", userId);
        }
        query.setFirstResult(offset);
        query.setMaxResults(amount);
        return query.getResultList();
    }
}
