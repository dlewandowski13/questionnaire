package com.s26462.questionnaire.questionairedefinition.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
public class QuestionDefinitionDto {
    private String question;
    private LinkedHashMap<String, AnswerDefinitionDto> answerDefinition;
}
