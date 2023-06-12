package com.s26462.questionnaire.questionnaire.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Questionnaire with products dto.
 */
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
