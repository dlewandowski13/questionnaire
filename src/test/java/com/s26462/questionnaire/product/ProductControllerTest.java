package com.s26462.questionnaire.product;

import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProductControllerTest {

    @InjectMocks
    ProductController productController;
    @Mock
    ProductService productService;
    @Mock
    ProductMapper productMapper;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldReturnListOfProducts() {
        //given
        List<Product> products = new ArrayList<>();
        products.add(new Product("product1", "Product pierwszy", "TUW", true));
        products.add(new Product("product2", "Product drugi", "TUŻ", true));
        when(productService.getProducts()).thenReturn(products);
        List<ProductDto> expected = new ArrayList<>();
        expected.add(new ProductDto("product1", "Product pierwszy", "TUW", true));
        expected.add(new ProductDto("product2", "Product drugi", "TUŻ", true));
        IntStream.range(0, products.size())
                .forEach(i -> when(productMapper.productToDtoMapper(products.get(i))).thenReturn(expected.get(i)));
//        when(productMapper.productToDtoMapper(products.get(0))).thenReturn(expected.get(0));
        //when
        List<ProductDto> actual = productController.getProducts().getBody();
        //then
        assertEquals(expected, actual);
    }

    @Test
    @Disabled
    public void shouldReturnProductBySymbol() {
        //given
        String productSymbol = "product1";
        Product product = new Product("product1", "Product pierwszy", "TUW", true);

        when(productService.getProductById(productSymbol))
                .thenReturn(Optional.of(product));

        //when
        ResponseEntity<ProductDto> response = productController.getProductsBySymbol(productSymbol);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSymbol()).isEqualTo("product1");
        assertThat(response.getBody().getName()).isEqualTo("Product pierwszy");
        assertThat(response.getBody().getCompany()).isEqualTo("TUW");
        assertThat(response.getBody().isActive()).isEqualTo(true);
    }

    @Test
    public void shouldReturnNotFound() {
        //given
        String productSymbol = "ABC123";
        when(productService.getProductById(productSymbol))
                .thenReturn(Optional.empty());

        //when
        ResponseEntity<ProductDto> response = productController.getProductsBySymbol(productSymbol);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
