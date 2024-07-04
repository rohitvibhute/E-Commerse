package com.rohit.e_commerse.IntegrationTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohit.e_commerse.DTO.OrderDTO;
import com.rohit.e_commerse.Entity.Order;
import com.rohit.e_commerse.Entity.Product;
import com.rohit.e_commerse.Repo.OrderRepo;
import com.rohit.e_commerse.Repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        orderRepo.deleteAll();
        productRepo.deleteAll();
    }

    @Test
    public void testGetAllOrders() throws Exception {
        Product product = new Product(null, "Product1", 10.0, "Description1");
        product = productRepo.save(product);

        Order order1 = new Order(null, product, 2);
        Order order2 = new Order(null, product, 3);
        orderRepo.saveAll(List.of(order1, order2));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetOrderById() throws Exception {
        Product product = new Product(null, "Product1", 10.0, "Description1");
        product = productRepo.save(product);

        Order order = new Order(null, product, 2);
        order = orderRepo.save(order);

        mockMvc.perform(get("/api/orders/{id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    public void testCreateOrder() throws Exception {
        Product product = new Product(null, "Product1", 10.0, "Description1");
        product = productRepo.save(product);

        OrderDTO orderDTO = new OrderDTO(null, product.getId(), 2);

        MvcResult result = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andReturn();

        OrderDTO createdOrder = objectMapper.readValue(result.getResponse().getContentAsString(), OrderDTO.class);
        assertEquals(2, createdOrder.getQuantity());
    }

    @Test
    public void testDeleteOrder() throws Exception {
        Product product = new Product(null, "Product1", 10.0, "Description1");
        product = productRepo.save(product);

        Order order = new Order(null, product, 2);
        order = orderRepo.save(order);

        mockMvc.perform(delete("/api/orders/{id}", order.getId()))
                .andExpect(status().isOk());

        assertEquals(0, orderRepo.count());
    }
}
