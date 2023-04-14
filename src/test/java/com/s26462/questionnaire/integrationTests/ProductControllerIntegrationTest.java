package com.s26462.questionnaire.integrationTests;

import com.s26462.questionnaire.QuestionnaireApplication;
import com.s26462.questionnaire.product.Product;
import com.s26462.questionnaire.product.ProductDto;
import com.s26462.questionnaire.product.ProductMapper;
import com.s26462.questionnaire.product.ProductService;
import com.s26462.questionnaire.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = QuestionnaireApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {

    private final String url = "http://localhost:8080";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    private HttpHeaders headers;

    private Product product1;
    private Product product2;

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        product1 = new Product("TestSymbolProduct1", "product1", "company1", true);
        product2 = new Product("TestSymbolProduct2", "product2", "company2", true);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteProductBySymbol("TestSymbolProduct1");
        productRepository.deleteProductBySymbol("TestSymbolProduct2");
    }

    @Test
    public void getProductsShouldReturnAllProducts() {
        //given
        productRepository.saveAll(Arrays.asList(product1, product2));

        //when
        ResponseEntity<ProductDto[]> responseEntity = restTemplate.getForEntity(
                url + "/products",
                ProductDto[].class
        );

        //then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).length).isEqualTo(2);
    }

    @Test
    public void getProductBySymbolShouldReturnRightProduct() {
        //given
        productRepository.save(product1);

        //when
        ResponseEntity<ProductDto> responseEntity = restTemplate.getForEntity(
                url + "/products/TestSymbolProduct1",
                ProductDto.class
        );

        //then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getSymbol()).isEqualTo("TestSymbolProduct1");
        assertThat(responseEntity.getBody().getName()).isEqualTo("product1");
        assertThat(responseEntity.getBody().getCompany()).isEqualTo("company1");
    }

    @Test
    public void postProductListShouldSaveAllProducts() {
        //given
        ProductDto productDto1 = productMapper.productToDtoMapper(product1);
        ProductDto productDto2 = productMapper.productToDtoMapper(product2);
        List<ProductDto> productsDto = new ArrayList<>();
        productsDto.add(productDto1);
        productsDto.add(productDto2);

        HttpEntity<List<ProductDto>> requestEntity = new HttpEntity<>(productsDto, headers);

        //when
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(
                url + "/products",
                requestEntity,
                Void.class
        );

        //then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(productRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    public void postProductShouldSaveProduct() {
        //given
        ProductDto productDto = productMapper.productToDtoMapper(product1);

        HttpEntity<ProductDto> requestEntity = new HttpEntity<>(productDto, headers);

        //when
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(
                url + "/products/product",
                requestEntity,
                Void.class
        );

        //then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(productRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void putProductShouldSaveProductChanges() {
        //given
        ProductDto productDto = productMapper.productToDtoMapper(product1);
        HttpEntity<ProductDto> requestEntity = new HttpEntity<>(productDto, headers);
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(
                url + "/products/product",
                requestEntity,
                Void.class
        );

        ProductDto updateProductDto = productMapper.productToDtoMapper(product2);
        HttpEntity<ProductDto> requestPutEntity = new HttpEntity<>(updateProductDto, headers);

        //when
        ResponseEntity<Void> response = restTemplate.exchange(
                url + "/products/TestSymbolProduct1",
                HttpMethod.PUT,
                new HttpEntity<>(updateProductDto),
                Void.class
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Optional<Product> updated = productService.getProductById("TestSymbolProduct2");
        assertThat(updated).isEmpty();
    }
}