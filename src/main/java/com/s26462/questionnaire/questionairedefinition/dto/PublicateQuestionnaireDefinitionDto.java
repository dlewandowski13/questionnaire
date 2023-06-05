package com.s26462.questionnaire.questionairedefinition.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class PublicateQuestionnaireDefinitionDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date publicationDate;
}
