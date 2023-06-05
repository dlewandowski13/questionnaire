package com.s26462.questionnaire.questionairedefinition.validators;

import com.s26462.questionnaire.questionairedefinition.dto.PublicateQuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.service.QuestionnaireDefinitionService;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.Optional;

@Log4j2
public class PublicateQuestionnaireDefinitionValidator {

    private final QuestionnaireDefinitionService questionnaireDefinitionService;

    public PublicateQuestionnaireDefinitionValidator(QuestionnaireDefinitionService questionnaireDefinitionService) {
        this.questionnaireDefinitionService = questionnaireDefinitionService;
    }

    public Optional<QuestionnaireDefinitionDto> validateExistingPublicateQuestionnaireDefinition(
            PublicateQuestionnaireDefinitionDto publicateQuestionnaireDefinitionDto){
        log.info("validateExistingPublicateQuestionnaireDefinition: " + questionnaireDefinitionService.findPublicateQuestionnaireDefinition(new Date()));
        return Optional.ofNullable(questionnaireDefinitionService.findPublicateQuestionnaireDefinition(new Date()))
                .orElse(null);
    }
}
