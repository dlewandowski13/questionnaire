package com.s26462.questionnaire.questionnaire.collection.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

/**
 * The type Question.
 */
@Getter
@Setter
public class Question {
    private String question;
    private LinkedHashMap<String, Answer> answer;
}
