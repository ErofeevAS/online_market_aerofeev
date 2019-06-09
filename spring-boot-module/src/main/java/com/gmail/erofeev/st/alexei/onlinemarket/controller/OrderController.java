package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.FrontEndValidator;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.OrderService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserAuthenticationService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderDetailsDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final UserAuthenticationService userAuthenticationService;
    private final FrontEndValidator frontEndValidator;

    public OrderController(OrderService orderService,
                           UserAuthenticationService userAuthenticationService,
                           FrontEndValidator frontEndValidator) {
        this.orderService = orderService;
        this.userAuthenticationService = userAuthenticationService;
        this.frontEndValidator = frontEndValidator;
    }

    @GetMapping("/orders")
    public String getOrders(@RequestParam(defaultValue = "1", required = false) String page,
                            @RequestParam(defaultValue = "10", required = false) String size,
                            Model model) {
        Paginator paginator = new Paginator(page, size);
        PageDTO<OrderDTO> pageDTO = orderService.getOrders(paginator.getPage(), paginator.getSize(), null);
        paginator.setMaxPage(pageDTO.getAmountOfPages());
        model.addAttribute("orders", pageDTO.getList());
        model.addAttribute("paginator", paginator);
        return "orders";
    }

    @PostMapping("/orders/{uuid}/update")
    public String updateRole(@PathVariable("uuid") String uniqueNumber,
                             @RequestParam("orderStatus") String orderStatus) {
        orderService.updateOrderStatus(uniqueNumber, orderStatus);
        return "redirect:/orders";
    }

    @PostMapping("/orders/{uuid}")
    public String viewItem(@PathVariable String uuid,
                           Model model) {
        OrderDetailsDTO order = orderService.findOrderByUUID(uuid);
        model.addAttribute("order", order);
        return "order";
    }

    @GetMapping("/userorders")
    public String getUsersOrders(Authentication authentication) {
        Long userId = userAuthenticationService.getSecureUserId(authentication);
        return "redirect:/users/" + userId + "/orders";
    }

    @PostMapping("/orders/customer/new")
    public String createOrder(Authentication authentication,
                              RedirectAttributes attributes,
                              @RequestParam("itemId") Long itemId,
                              @RequestParam("amount") String amount) {
        Integer validateAmount = frontEndValidator.validateAmount(amount);
        if (validateAmount == null) {
            attributes.addFlashAttribute("info", "amount must be more than zero");
            return "redirect:/items";
        }
        Long userId = userAuthenticationService.getSecureUserId(authentication);
        orderService.createOrder(userId, itemId, validateAmount);
        return "redirect:/users/" + userId + "/orders";
    }

    @GetMapping("/users/{id}/orders")
    public String getUsersOrders(@RequestParam(defaultValue = "1", required = false) String page,
                                 @RequestParam(defaultValue = "10", required = false) String size,
                                 @PathVariable String id,
                                 Model model,
                                 Authentication authentication) {
        Paginator paginator = new Paginator(page, size);
        Long validateId = frontEndValidator.validateId(id);
        userAuthenticationService.isUserThSameLikeAuthorizedUser(validateId, authentication);
        PageDTO<OrderDTO> pageDTO = orderService.getOrders(paginator.getPage(), paginator.getSize(), validateId);
        paginator.setMaxPage(pageDTO.getAmountOfPages());
        model.addAttribute("orders", pageDTO.getList());
        model.addAttribute("paginator", paginator);
        return "orders";
    }
}
