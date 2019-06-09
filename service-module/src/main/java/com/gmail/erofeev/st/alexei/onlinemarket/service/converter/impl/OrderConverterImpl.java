package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Order;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.OrderConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDetailsDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderRestDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverterImpl implements OrderConverter {
    @Override
    public OrderDTO toDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUniqueNumber(order.getUniqueNumber());
        orderDTO.setAmountOfItem(order.getAmount());
        orderDTO.setCreatedDate(order.getCreatedDate());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTotalPrice(getTotalPrice(order));
        orderDTO.setItemName(order.getItem().getName());
        return orderDTO;
    }

    @Override
    public OrderDetailsDTO toDetailsDTO(Order order) {
        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
        OrderDTO orderDTO = toDTO(order);
        orderDetailsDTO.setOrderDTO(orderDTO);
        String firstName = order.getUser().getFirstName();
        String lastName = order.getUser().getLastName();
        orderDetailsDTO.setUserName(firstName + " " + lastName);
        String phone = order.getUser().getProfile().getPhone();
        orderDetailsDTO.setPhoneNumber(phone);
        return orderDetailsDTO;
    }

    @Override
    public OrderRestDTO toRestDTO(Order order) {
        OrderRestDTO orderRestDTO = new OrderRestDTO();
        OrderDTO orderDTO = toDTO(order);
        orderRestDTO.setOrderDTO(orderDTO);
        orderRestDTO.setItemId(order.getItem().getId());
        orderRestDTO.setUserId(order.getUser().getId());
        orderRestDTO.setId(order.getId());
        return orderRestDTO;
    }

    @Override
    public List<OrderRestDTO> toListRestDTO(List<Order> orders) {
        return orders.stream()
                .map(this::toRestDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> toListDTO(List<Order> orders) {
        return orders.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private BigDecimal getTotalPrice(Order order) {
        int amountOfItem = order.getAmount();
        BigDecimal price = order.getItem().getPrice();
        return price.multiply(new BigDecimal(amountOfItem));
    }
}
