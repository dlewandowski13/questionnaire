package com.s26462.questionnaire.questionairedefinition.collection.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Answer {
    private String description;
    private List<String> eliminatedProducts;
}
