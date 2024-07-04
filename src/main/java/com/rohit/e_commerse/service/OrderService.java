package com.rohit.e_commerse.service;


import com.rohit.e_commerse.DTO.OrderDTO;
import com.rohit.e_commerse.Entity.Order;
import com.rohit.e_commerse.Entity.Product;
import com.rohit.e_commerse.Repo.OrderRepo;
import com.rohit.e_commerse.Repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;

    public List<OrderDTO> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDTO(order);
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        order = orderRepo.save(order);
        return convertToDTO(order);
    }

    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }

    private OrderDTO convertToDTO(Order order) {
        return new OrderDTO(order.getId(), order.getProduct().getId(), order.getQuantity());
    }

    private Order convertToEntity(OrderDTO orderDTO) {
        Product product = productRepo.findById(orderDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return new Order(orderDTO.getId(), product, orderDTO.getQuantity());
    }
}
