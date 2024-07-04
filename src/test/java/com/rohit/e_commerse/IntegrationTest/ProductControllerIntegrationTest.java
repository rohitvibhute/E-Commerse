package com.rohit.e_commerse.IntegrationTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohit.e_commerse.DTO.ProductDTO;
import com.rohit.e_commerse.Entity.Product;
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
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        productRepo.deleteAll();
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Product product1 = new Product(null, "Product1", 10.0, "Description1");
        Product product2 = new Product(null, "Product2", 20.0, "Description2");
        productRepo.saveAll(List.of(product1, product2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = new Product(null, "Product1", 10.0, "Description1");
        product = productRepo.save(product);

        mockMvc.perform(get("/api/products/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product1"));
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO(null, "Product1", 10.0, "Description1");

        MvcResult result = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andReturn();

        ProductDTO createdProduct = objectMapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals("Product1", createdProduct.getName());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = new Product(null, "Product1", 10.0, "Description1");
        product = productRepo.save(product);

        mockMvc.perform(delete("/api/products/{id}", product.getId()))
                .andExpect(status().isOk());

        assertEquals(0, productRepo.count());
    }
}
