package com.s26462.questionnaire.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductTest {

    @Test
    void newlyCreatedProductShouldHaveSymbolAndNameAndIsActiveTrue(){

        //given
        Product product = new Product();
        product.setSymbol("TwojeBezpieczeństwo");
        product.setName("Indywidualne ubezpieczenie na życie Twoje bezpieczeństwo");
        product.setActive(true);

        //then
        assertNotNull(product.getSymbol());
        assertNotNull(product.getName());
        assertTrue(product.isActive());

    }
}
