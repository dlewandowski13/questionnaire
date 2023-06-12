package com.s26462.questionnaire.questionairedefinition.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * The type Questionnaires definitions dto.
 */
@Getter
@Setter
public class QuestionnairesDefinitionsDto {
    private String symbol;
    private String author;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date creationDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date publicationDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date expiryDate;
}
