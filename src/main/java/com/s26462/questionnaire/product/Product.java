package com.s26462.questionnaire.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collation = "Product")
public class Product {

    private String symbol;
    private String name;
    private boolean isActive;

    public Product(String symbol, String name, boolean isActive) {
        this.symbol = symbol;
        this.name = name;
        this.isActive = isActive;
    }
}
