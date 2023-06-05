package com.s26462.questionnaire.questionairedefinition.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
public class QuestionDto {
    private String question;
    private LinkedHashMap<String, AnswerDto> answerDefinition;
}
