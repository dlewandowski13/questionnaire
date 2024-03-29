package com.s26462.questionnaire.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Product dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String symbol;
    private String name;
    private String company;
    private boolean isActive;

}
