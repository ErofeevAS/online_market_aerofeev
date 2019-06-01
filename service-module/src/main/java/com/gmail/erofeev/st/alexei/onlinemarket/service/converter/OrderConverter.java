package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.embedded.Order;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDetailsDTO;

import java.util.List;

public interface OrderConverter {
    OrderDTO toDTO(Order order);

    List<OrderDTO> toListDTO(List<Order> orders);

    OrderDetailsDTO toDetailsDTO(Order order);
}
