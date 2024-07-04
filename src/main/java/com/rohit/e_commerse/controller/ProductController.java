package com.rohit.e_commerse.controller;

import com.rohit.e_commerse.DTO.ProductDTO;
import com.rohit.e_commerse.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts(HttpSession session) {
        logger.info("Fetching all products");
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id, HttpSession session) {
        logger.info("Fetching product with id: {}", id);
        ProductDTO product = productService.getProductById(id);
        session.setAttribute("lastViewedProduct", product);
        return product;
    }

    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO, HttpSession session) {
        logger.info("Creating product with name: {}", productDTO.getName());
        return productService.createProduct(productDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id, HttpSession session) {
        logger.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);
    }
}
