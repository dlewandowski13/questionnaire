package com.s26462.questionnaire.questionnaire.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Answer dto.
 */
@Getter
@Setter
public class AnswerDto {
    private String description;
    private List<String> eliminatedProducts;

    @Override
    public String toString() {
        String eliminatedProductsString = (eliminatedProducts != null) ? eliminatedProducts.toString() : "brak";

        return "Odpowiedź: " + description + '\n' +
                "Wyeliminowane produkty: " + eliminatedProductsString + '\n';
    }

}
