package com.s26462.questionnaire.questionairedefinition.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class QuestionnaireDefinitionDto {
    private String symbol;
    private String author;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date creationDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date publicationDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date expiryDate;
    private List<QuestionDto> questions;
}
