package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ItemRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.OrderRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.embedded.Order;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.embedded.OrderId;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.enums.OrderStatusEnum;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.OrderConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDetailsDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static java.util.UUID.randomUUID;

@Service
public class OrderServiceImpl extends AbstractService implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderConverter orderConverter;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            ItemRepository itemRepository,
                            OrderConverter orderConverter
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.orderConverter = orderConverter;
    }

    @Override
    @Transactional
    public void createOrder(Long userId, Long itemId, int amount) {
        User user = userRepository.findById(userId);
        Item item = itemRepository.findById(itemId);
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        OrderId orderId = new OrderId(user.getId(), item.getId(), timestamp);
        Order order = new Order(orderId);
        order.setStatus(OrderStatusEnum.NEW);
        order.setAmount(amount);
        order.setUniqueNumber(randomUUID().toString());
//        BigDecimal totalPrice = item.getPrice().multiply(new BigDecimal(amount));
//        order.setTotalPrice(totalPrice);
        orderRepository.persist(order);
    }

    @Override
    @Transactional
    public PageDTO<OrderDTO> getOrders(int page, int amount, Long userId) {
        Integer amountOfEntity = orderRepository.getAmountOfEntity();
        int maxPages = getMaxPages(amountOfEntity, amount);
        int offset = getOffset(page, maxPages, amount);
        List<Order> orders = orderRepository.getOrders(offset, amount, userId);
        List<OrderDTO> orderListDTO = orderConverter.toListDTO(orders);
        PageDTO<OrderDTO> pageDTO = new PageDTO<>();
        pageDTO.setAmountOfPages(maxPages);
        pageDTO.setList(orderListDTO);
        return pageDTO;
    }

    @Override
    @Transactional
    public void updateOrderStatus(String uniqueNumber, String orderStatus) {
        Order order = orderRepository.findByUUID(uniqueNumber);
        order.setStatus(OrderStatusEnum.valueOf(orderStatus));
        orderRepository.merge(order);
    }

    @Override
    @Transactional
    public OrderDetailsDTO findOrderByUUID(String uniqueNumber) {
        Order order = orderRepository.findByUUID(uniqueNumber);
        return orderConverter.toDetailsDTO(order);
    }
}
