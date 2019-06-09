package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.RequestParamsValidator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.OrderService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderRestDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class RestOrderController {
    private final OrderService orderService;
    private final RequestParamsValidator requestParamsValidator;

    public RestOrderController(OrderService orderService, RequestParamsValidator requestParamsValidator) {
        this.orderService = orderService;
        this.requestParamsValidator = requestParamsValidator;
    }

    @GetMapping
    public List<OrderRestDTO> getOrders(@RequestParam(defaultValue = "0", required = false) String offset,
                                        @RequestParam(defaultValue = "10", required = false) String amount) {
        int intOffset = requestParamsValidator.validateIntRest(offset);
        int intAmount = requestParamsValidator.validateIntRest(amount);
        return orderService.getOrdersForRest(intOffset, intAmount);
    }

    @GetMapping("/{id}")
    public OrderRestDTO getOrder(@PathVariable String id) {
        Long validatedId = requestParamsValidator.validateLongRest(id);
        return orderService.findOrderByIdForRest(validatedId);
    }
}
