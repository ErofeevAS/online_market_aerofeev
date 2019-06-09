package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Order;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDetailsDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderRestDTO;

import java.util.List;

public interface OrderConverter {
    OrderDTO toDTO(Order order);

    List<OrderDTO> toListDTO(List<Order> orders);

    OrderDetailsDTO toDetailsDTO(Order order);

    OrderRestDTO toRestDTO(Order order);

    List<OrderRestDTO> toListRestDTO(List<Order> orders);
}
