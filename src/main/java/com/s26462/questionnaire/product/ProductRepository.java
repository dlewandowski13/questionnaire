package com.s26462.questionnaire.product;


import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private final List<Product> products = new ArrayList<>();
    public void add(Product product) {
        products.add(product);
    }

    public List<Product> getAllProducts() {
        return products;
    }
}
