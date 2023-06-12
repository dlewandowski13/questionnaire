package com.s26462.questionnaire.initialization;

import com.s26462.questionnaire.product.Product;
import com.s26462.questionnaire.product.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Product initializer.
 */
@Component
@AllArgsConstructor
public class ProductInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        productRepository.deleteAll();
        List<Product> products = new ArrayList<>();
        products.add(new Product("TwojeŻycie", "Twoje Życie", "Firma 1", true));
        products.add(new Product("TwojeZdrowie", "Twoje Zdrowie", "Firma 1", true));
        products.add(new Product("TwójPojazd", "Twój Pojazd", "Firma 2", true));
        products.add(new Product("TwojeMieszkanie", "Twoje Mieszkanie", "Firma 3", true));
        products.add(new Product("TwójDom", "Twój Dom", "Firma 3", true));
        productRepository.saveAll(products);
    }
}
