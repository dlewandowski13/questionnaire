package com.s26462.questionnaire.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private String symbol;
    private String name;
    private boolean isActive;

    public Product(String symbol, String name, boolean isActive) {
        this.symbol = symbol;
        this.name = name;
        this.isActive = isActive;
    }

    public Product() {

    }
}
