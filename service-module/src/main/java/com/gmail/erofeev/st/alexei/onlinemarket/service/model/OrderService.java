package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import org.springframework.security.core.Authentication;

public interface OrderService {
    void createOrder(Long userId, Long itemId, int amount);

    PageDTO<OrderDTO> getOrders(int page, int amount, Long userId);

    void updateOrderStatus(String uniqueNumber, String orderStatus);

    OrderDetailsDTO findOrderByUUID(String uniqueNumber);
}
