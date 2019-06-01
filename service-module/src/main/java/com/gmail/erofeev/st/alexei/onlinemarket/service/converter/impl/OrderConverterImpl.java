package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.embedded.Order;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.OrderConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDetailsDTO;
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
        orderDTO.setCreatedDate(order.getId().getCreatedDate());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTotalPrice(getTotalPrice(order));
        orderDTO.setItemName(order.getItem().getName());
        return orderDTO;
    }

    @Override
    public List<OrderDTO> toListDTO(List<Order> orders) {
        return orders.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailsDTO toDetailsDTO(Order order) {
        OrderDetailsDTO orderDTO = new OrderDetailsDTO();
        orderDTO.setUniqueNumber(order.getUniqueNumber());
        orderDTO.setAmountOfItem(order.getAmount());
        orderDTO.setCreatedDate(order.getId().getCreatedDate());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTotalPrice(getTotalPrice(order));
        orderDTO.setItemName(order.getItem().getName());
        String firstName = order.getUser().getFirstName();
        String lastName = order.getUser().getLastName();
        orderDTO.setUserName(firstName + " " + lastName);
        String phone = order.getUser().getProfile().getPhone();
        orderDTO.setPhoneNumber(phone);
        return orderDTO;
    }

    private BigDecimal getTotalPrice(Order order) {
        int amountOfItem = order.getAmount();
        BigDecimal price = order.getItem().getPrice();
        return price.multiply(new BigDecimal(amountOfItem));
    }

}
