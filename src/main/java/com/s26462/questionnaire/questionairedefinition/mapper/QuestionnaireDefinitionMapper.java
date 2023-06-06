package com.s26462.questionnaire.questionairedefinition.mapper;

import com.s26462.questionnaire.questionairedefinition.collection.QuestionnaireDefinition;
import com.s26462.questionnaire.questionairedefinition.collection.utils.AnswerDefinition;
import com.s26462.questionnaire.questionairedefinition.collection.utils.QuestionDefinition;
import com.s26462.questionnaire.questionairedefinition.dto.AnswerDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnairesDefinitionsDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QuestionnaireDefinitionMapper {
    private final ModelMapper modelMapper;

    public QuestionnaireDefinitionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public QuestionnairesDefinitionsDto questionnairesDefinitionsToDtoMapper(QuestionnaireDefinition questionnaireDefinition) {
        return modelMapper.map(questionnaireDefinition, QuestionnairesDefinitionsDto.class);
    }

    public QuestionnaireDefinitionDto questionnaireDefinitionToDtoMapper(QuestionnaireDefinition questionnaireDefinition) {
        QuestionnaireDefinitionDto questionnaireDefinitionDto = modelMapper.map(questionnaireDefinition, QuestionnaireDefinitionDto.class);

        List<QuestionDefinition> questionDefinitions = questionnaireDefinition.getQuestionDefinitions();
        List<QuestionDefinitionDto> questionDefinitionDtos = questionDefinitions.stream()
                .map(this::mapQuestionToQuestionDto)
                .collect(Collectors.toList());

        questionnaireDefinitionDto.setQuestionDefinitions(questionDefinitionDtos);
        return questionnaireDefinitionDto;
    }

    public QuestionnaireDefinition questionnaireDefinitionDtoToQuestionnaireDefinitionMapper(QuestionnaireDefinitionDto questionnaireDefinitionDto) {
        QuestionnaireDefinition questionnaireDefinition = modelMapper.map(questionnaireDefinitionDto, QuestionnaireDefinition.class);

        List<QuestionDefinitionDto> questionDefinitionDtos = questionnaireDefinitionDto.getQuestionDefinitions();
        List<QuestionDefinition> questionDefinitions = questionDefinitionDtos.stream()
                .map(this::mapQuestionDtoToQuestion)
                .collect(Collectors.toList());

        questionnaireDefinition.setQuestionDefinitions(questionDefinitions);
        return questionnaireDefinition;
    }

    private QuestionDefinition mapQuestionDtoToQuestion(QuestionDefinitionDto questionDefinitionDto) {
        QuestionDefinition questionDefinition = new QuestionDefinition();
        questionDefinition.setQuestion(questionDefinitionDto.getQuestion());

        LinkedHashMap<String, AnswerDefinitionDto> answerDtos = questionDefinitionDto.getAnswerDefinition();
        LinkedHashMap<String, AnswerDefinition> answers = answerDtos.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> mapAnswerDtoToAnswer(entry.getValue()), (a, b) -> a, LinkedHashMap::new));

        questionDefinition.setAnswerDefinition(answers);
        return questionDefinition;
    }

    private AnswerDefinition mapAnswerDtoToAnswer(AnswerDefinitionDto answerDefinitionDto) {
        AnswerDefinition answerDefinition = new AnswerDefinition();
        answerDefinition.setDescription(answerDefinitionDto.getDescription());
        answerDefinition.setEliminatedProducts(answerDefinitionDto.getEliminatedProducts());
        return answerDefinition;
    }

    private QuestionDefinitionDto mapQuestionToQuestionDto(QuestionDefinition questionDefinition) {
        QuestionDefinitionDto questionDefinitionDto = new QuestionDefinitionDto();
        questionDefinitionDto.setQuestion(questionDefinition.getQuestion());

        LinkedHashMap<String, AnswerDefinition> answers = questionDefinition.getAnswerDefinition();
        LinkedHashMap<String, AnswerDefinitionDto> answerDtos = answers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> mapAnswerToAnswerDto(entry.getValue()), (a, b) -> a, LinkedHashMap::new));

        questionDefinitionDto.setAnswerDefinition(answerDtos);
        return questionDefinitionDto;
    }

    private AnswerDefinitionDto mapAnswerToAnswerDto(AnswerDefinition answerDefinition) {
        AnswerDefinitionDto answerDefinitionDto = new AnswerDefinitionDto();
        answerDefinitionDto.setDescription(answerDefinition.getDescription());
        answerDefinitionDto.setEliminatedProducts(answerDefinition.getEliminatedProducts());
        return answerDefinitionDto;
    }

}
