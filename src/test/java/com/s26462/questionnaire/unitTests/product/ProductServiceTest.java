package com.s26462.questionnaire.unitTests.product;

import com.s26462.questionnaire.product.Product;
import com.s26462.questionnaire.product.ProductService;
import com.s26462.questionnaire.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductServiceTest {

    private ProductRepository productRepositoryMock;
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productRepositoryMock = Mockito.mock(ProductRepository.class);
        productService = new ProductService(productRepositoryMock);
    }

    @Test
    public void testGetProductById() {
        Product product = new Product("1", "SYM1", "Product 1", true);
        Mockito.when(productRepositoryMock.findById(product.getId())).thenReturn(Optional.of(product));
        Optional<Product> result = productService.getProductById(product.getId());
        Assertions.assertEquals(product, result.get());
    }

    @Test
    public void testGetProductBySymbol() {
        Product product = new Product("1", "SYM1", "Product 1", true);
        Mockito.when(productRepositoryMock.findBySymbol(product.getSymbol())).thenReturn(Optional.of(product));
        Optional<Product> result = productService.getProductBySymbol(product.getSymbol());
        Assertions.assertEquals(product, result.get());
    }

    @Test
    public void testGetProducts() {
        Product product1 = new Product("1", "SYM1", "Product 1", true);
        Product product2 = new Product("2", "SYM2", "Product 2", false);
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        Mockito.when(productRepositoryMock.findAll()).thenReturn(products);
        List<Product> result = productService.getProducts();
        Assertions.assertEquals(products.size(), result.size());
        Assertions.assertEquals(product1, result.get(0));
        Assertions.assertEquals(product2, result.get(1));
    }

    @Test
    public void testInsertProducts() {
        Product product1 = new Product("1", "SYM1", "Product 1", true);
        Product product2 = new Product("2", "SYM2", "Product 2", false);
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        productService.insertProducts(products);
        Mockito.verify(productRepositoryMock, Mockito.times(1)).saveAll(products);
    }

    @Test
    public void testInsertProduct() {
        Product product = new Product("1", "SYM1", "Product 1", true);
        productService.insertProduct(product);
        Mockito.verify(productRepositoryMock, Mockito.times(1)).insert(product);
    }

    @Test
    public void testUpdateProductById() {
        Product product = new Product("1", "SYM1", "Product 1", true);
        Mockito.when(productRepositoryMock.findById(product.getId())).thenReturn(Optional.of(product));
        productService.updateProductById(product.getId(), new Product("1", "SYM1-new", "Product 1 new", false));
        Mockito.verify(productRepositoryMock, Mockito.times(1)).save(product);
        Mockito.verify(productRepositoryMock, Mockito.times(1)).findById(product.getId());
    }

}
