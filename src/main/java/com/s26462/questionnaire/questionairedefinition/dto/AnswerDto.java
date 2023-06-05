package com.s26462.questionnaire.questionairedefinition.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnswerDto {
    private String description;
    private List<String> eliminatedProducts;
}
