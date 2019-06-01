package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.embedded.Order;

import java.util.List;

public interface OrderRepository extends GenericRepository<Long, Order> {

    Order findByUUID(String uniqueNumber);

    List<Order> getOrders(int offset, int amount, Long userId);
}
