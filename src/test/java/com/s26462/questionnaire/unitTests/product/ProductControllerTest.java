package com.s26462.questionnaire.unitTests.product;

import com.s26462.questionnaire.product.*;
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
        Optional<Product> expected = Optional.of(new Product("product1", "Produkt pierwszy", "TUW", true));

        when(productService.getProductBySymbol(productSymbol))
                .thenReturn(expected);

        //when
        ResponseEntity<ProductDto> response = productController.getProductBySymbol(productSymbol);
        ProductDto actual = response.getBody();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected.map(productMapper::productToDtoMapper).get(), actual);
    }


    @Test
    public void shouldReturnNotFound() {
        //given
        String productSymbol = "ABC123";
        when(productService.getProductById(productSymbol))
                .thenReturn(Optional.empty());

        //when
        ResponseEntity<ProductDto> response = productController.getProductBySymbol(productSymbol);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
