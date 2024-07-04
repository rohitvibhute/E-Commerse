package com.rohit.e_commerse.UnitTest;


import com.rohit.e_commerse.DTO.ProductDTO;
import com.rohit.e_commerse.Entity.Product;
import com.rohit.e_commerse.Repo.ProductRepo;
import com.rohit.e_commerse.service.ProductService;
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
public class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    public ProductServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product(1L, "Product1", 10.0, "Description1");
        Product product2 = new Product(2L, "Product2", 20.0, "Description2");
        when(productRepo.findAll()).thenReturn(Arrays.asList(product1, product2));

        var products = productService.getAllProducts();
        assertEquals(2, products.size());
        assertEquals("Product1", products.get(0).getName());
        assertEquals("Product2", products.get(1).getName());
    }

    @Test
    public void testGetProductById() {
        Product product = new Product(1L, "Product1", 10.0, "Description1");
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO productDTO = productService.getProductById(1L);
        assertEquals("Product1", productDTO.getName());
    }

    @Test
    public void testCreateProduct() {
        ProductDTO productDTO = new ProductDTO(null, "Product1", 10.0, "Description1");
        Product product = new Product(1L, "Product1", 10.0, "Description1");
        when(productRepo.save(any(Product.class))).thenReturn(product);

        ProductDTO createdProduct = productService.createProduct(productDTO);
        assertEquals("Product1", createdProduct.getName());
    }

    @Test
    public void testDeleteProduct() {
        productService.deleteProduct(1L);
        verify(productRepo, times(1)).deleteById(1L);
    }
}
