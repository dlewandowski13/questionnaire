package com.s26462.questionnaire.questionnaire.dto;

import com.s26462.questionnaire.product.Product;
import lombok.Data;

import java.util.List;

@Data
public class QuestionnaireWithProductsDto {

    private String id;
    private String symbol;
    private String author;
    private String creationDate;
    private InsurerDto insurer;
    private List<QuestionDto> questions;
    private List<String> products;
}
