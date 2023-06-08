package com.s26462.questionnaire.questionnaire.collection.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Answer.
 */
@Getter
@Setter
public class Answer {
    private String description;
    private List<String> eliminatedProducts;
}
