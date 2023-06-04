package com.s26462.questionnaire.product;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The type Product.
 */
@Getter
@Setter
@Data
@NoArgsConstructor
@Document(collection = "product")
public class Product {

    @Id
    private String id;
    @Indexed(unique = true)
    private String symbol;
    private String name;
    private String company;
    private boolean isActive;

    /**
     * Instantiates a new Product.
     *
     * @param symbol   the symbol
     * @param name     the name
     * @param company  the company
     * @param isActive the is active
     */
    public Product(String symbol, String name, String company, boolean isActive) {
        this.symbol = symbol;
        this.name = name;
        this.company = company;
        this.isActive = isActive;
    }

}
