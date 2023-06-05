package com.s26462.questionnaire.questionairedefinition.mapper;

import com.s26462.questionnaire.questionairedefinition.collection.QuestionnaireDefinition;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnairesDefinitionsDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class QuestionnaireDefinitionMapper {
    private final ModelMapper modelMapper;

    public QuestionnaireDefinitionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public QuestionnaireDefinitionDto questionnaireDefinitionToDtoMapper(
            QuestionnaireDefinition questionnaireDefinition) {
        return modelMapper.map(questionnaireDefinition, QuestionnaireDefinitionDto.class);
    }

    public QuestionnaireDefinition questionnaireDefinitionDtoToQuestionnaireDefinitionMapper(
            QuestionnaireDefinitionDto questionnaireDefinitionDto) {
        return modelMapper.map(questionnaireDefinitionDto, QuestionnaireDefinition.class);
    }

    public QuestionnairesDefinitionsDto questionnairesDefinitionsToDtoMapper(
            QuestionnaireDefinition questionnaireDefinition) {
        return modelMapper.map(questionnaireDefinition, QuestionnairesDefinitionsDto.class);
    }
}
