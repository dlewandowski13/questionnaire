package com.s26462.questionnaire.questionnaire.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionnaireDto {

    private String id;
    private String symbol;
    private String author;
    private String creationDate;
    private InsurerDto insurer;
    private List<QuestionDto> questions;
}
