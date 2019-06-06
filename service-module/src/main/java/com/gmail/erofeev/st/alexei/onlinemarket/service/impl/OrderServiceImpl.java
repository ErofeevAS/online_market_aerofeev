package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ItemRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.OrderRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Order;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.enums.OrderStatusEnum;
import com.gmail.erofeev.st.alexei.onlinemarket.service.OrderService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.OrderConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDetailsDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderRestDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RestEntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static java.util.UUID.randomUUID;

@Service
public class OrderServiceImpl extends GenericService<OrderDTO> implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
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
        Order order = new Order();
        User user = userRepository.findById(userId);
        order.setUser(user);
        Item item = itemRepository.findById(itemId);
        order.setItem(item);
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        order.setCreatedDate(timestamp);
        order.setStatus(OrderStatusEnum.NEW);
        order.setAmount(amount);
        order.setUniqueNumber(randomUUID().toString());
        orderRepository.persist(order);
    }

    @Override
    @Transactional
    public PageDTO<OrderDTO> getOrders(int page, int amount, Long userId) {
        Integer amountOfEntity = orderRepository.getAmountOfEntity(false);
        int maxPages = getMaxPages(amountOfEntity, amount);
        int offset = getOffset(page, maxPages, amount);
        List<Order> orders = orderRepository.getOrders(offset, amount, userId);
        List<OrderDTO> orderListDTO = orderConverter.toListDTO(orders);
        return getPageDTO(orderListDTO, maxPages);
    }

    @Override
    @Transactional
    public List<OrderRestDTO> getOrdersForRest(int offset, int amount) {
        List<Order> orders = orderRepository.getEntities(offset, amount);
        return orderConverter.toListRestDTO(orders);
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

    @Override
    public OrderRestDTO findOrderByIdForRest(Long id) {
        Order order = orderRepository.findById(id);
        if (order == null) {
            logger.debug(String.format("Order with id:%s not found", id));
            throw new RestEntityNotFoundException(String.format("Order with id:%s not found", id));
        }
        return orderConverter.toRestDTO(order);
    }
}
