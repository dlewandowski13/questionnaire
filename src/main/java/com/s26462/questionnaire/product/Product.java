package com.s26462.questionnaire.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document
@AllArgsConstructor
public class Product {

    @Id
    private String id;
    private String symbol;
    private String name;
    private String company;
    private boolean isActive;

    public Product(String symbol, String name, String company, boolean isActive) {
        this.symbol = symbol;
        this.name = name;
        this.company = company;
        this.isActive = isActive;
    }

}
