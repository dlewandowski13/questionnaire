package com.s26462.questionnaire.product;

import org.junit.jupiter.api.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProductRepositoryTest {

    @Test
    void shouldBeAbleToAddProductToRepository(){

        //given
        ProductRepository productRepository = new ProductRepository();
        Product product = new Product("Twój Samochód","Ubezpieczenie komunikacyjne Twój Samochód", true);

        //when
        productRepository.add(product);

        //then
        assertThat(productRepository.getAllProducts().get(0), is(product));
    }
}
