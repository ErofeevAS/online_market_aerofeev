package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDetailsDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderRestDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;

import java.util.List;

public interface OrderService {
    void createOrder(Long userId, Long itemId, int amount);

    PageDTO<OrderDTO> getOrders(int page, int amount, Long userId);

    List<OrderRestDTO> getOrdersForRest(int offset, int amount);

    void updateOrderStatus(String uniqueNumber, String orderStatus);

    OrderDetailsDTO findOrderByUUID(String uniqueNumber);

    OrderRestDTO findOrderByIdForRest(Long id);
}
