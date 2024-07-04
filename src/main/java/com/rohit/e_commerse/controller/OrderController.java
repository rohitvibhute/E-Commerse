package com.rohit.e_commerse.controller;

import com.rohit.e_commerse.DTO.OrderDTO;
import com.rohit.e_commerse.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderDTO> getAllOrders(HttpSession session) {
        logger.info("Fetching all orders");
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable Long id, HttpSession session) {
        logger.info("Fetching order with id: {}", id);
        OrderDTO order = orderService.getOrderById(id);
        session.setAttribute("lastViewedOrder", order);
        return order;
    }

    @PostMapping
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {
        logger.info("Creating order with product id: {}", orderDTO.getProductId());
        return orderService.createOrder(orderDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id, HttpSession session) {
        logger.info("Deleting order with id: {}", id);
        orderService.deleteOrder(id);
    }
}
