package com.s26462.questionnaire.questionairedefinition.collection.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Setter
@Getter
public class QuestionDefinition {
    private String question;
    private LinkedHashMap<String, AnswerDefinition> answerDefinition;
}
