package com.rohit.e_commerse.UnitTest;

import com.rohit.e_commerse.DTO.OrderDTO;
import com.rohit.e_commerse.Entity.Order;
import com.rohit.e_commerse.Entity.Product;
import com.rohit.e_commerse.Repo.OrderRepo;
import com.rohit.e_commerse.Repo.ProductRepo;
import com.rohit.e_commerse.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {
    @Mock
    private OrderRepo orderRepo;

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private OrderService orderService;

    public OrderServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllOrders() {
        Product product = new Product(1L, "Product1", 10.0, "Description1");
        Order order1 = new Order(1L, product, 2);
        Order order2 = new Order(2L, product, 3);
        when(orderRepo.findAll()).thenReturn(Arrays.asList(order1, order2));

        var orders = orderService.getAllOrders();
        assertEquals(2, orders.size());
        assertEquals(2, orders.get(0).getQuantity());
        assertEquals(3, orders.get(1).getQuantity());
    }

    @Test
    public void testGetOrderById() {
        Product product = new Product(1L, "Product1", 10.0, "Description1");
        Order order = new Order(1L, product, 2);
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        OrderDTO orderDTO = orderService.getOrderById(1L);
        assertEquals(2, orderDTO.getQuantity());
    }

    @Test
    public void testCreateOrder() {
        Product product = new Product(1L, "Product1", 10.0, "Description1");
        OrderDTO orderDTO = new OrderDTO(null, 1L, 2);
        Order order = new Order(1L, product, 2);
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepo.save(any(Order.class))).thenReturn(order);

        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        assertEquals(2, createdOrder.getQuantity());
    }

    @Test
    public void testDeleteOrder() {
        orderService.deleteOrder(1L);
        verify(orderRepo, times(1)).deleteById(1L);
    }
}
