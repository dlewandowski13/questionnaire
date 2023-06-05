package com.s26462.questionnaire.questionairedefinition.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class QuestionnairesDefinitionsDto {
    private String symbol;
    private String author;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date creationDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date expiryDate;
}
