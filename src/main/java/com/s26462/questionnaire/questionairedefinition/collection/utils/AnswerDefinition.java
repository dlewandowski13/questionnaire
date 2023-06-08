package com.s26462.questionnaire.questionairedefinition.collection.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Answer definition.
 */
@Setter
@Getter
public class AnswerDefinition {
    private String description;
    private List<String> eliminatedProducts;
}
