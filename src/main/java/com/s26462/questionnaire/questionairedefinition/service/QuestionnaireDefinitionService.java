package com.s26462.questionnaire.questionairedefinition.service;

import com.s26462.questionnaire.exception.DateNotMatchException;
import com.s26462.questionnaire.questionairedefinition.collection.QuestionnaireDefinition;
import com.s26462.questionnaire.questionairedefinition.dto.PublicateQuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnairesDefinitionsDto;
import com.s26462.questionnaire.questionairedefinition.mapper.QuestionnaireDefinitionMapper;
import com.s26462.questionnaire.questionairedefinition.repository.QuestionnaireDefinitionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionnaireDefinitionService {
    private final QuestionnaireDefinitionRepository questionnaireDefinitionRepository;
    private final QuestionnaireDefinitionMapper questionnaireDefinitionMapper;

    public QuestionnaireDefinitionService(
            QuestionnaireDefinitionRepository questionnaireDefinitionRepository,
            QuestionnaireDefinitionMapper questionnaireDefinitionMapper) {
        this.questionnaireDefinitionRepository = questionnaireDefinitionRepository;
        this.questionnaireDefinitionMapper = questionnaireDefinitionMapper;
    }

    public List<QuestionnairesDefinitionsDto> getQuestionnairesDefinitions() {
        return questionnaireDefinitionRepository.findAll()
                .stream()
                .map(questionnaireDefinitionMapper::questionnairesDefinitionsToDtoMapper)
                .collect(Collectors.toList());
    }

    public Optional<QuestionnaireDefinitionDto> getQuestionnaireDefinitionBySymbol(
            String questionnaireDefinitionSymbol) {
        return Optional.ofNullable(questionnaireDefinitionSymbol)
                .flatMap(questionnaireDefinitionRepository::findBySymbol)
                .map(questionnaireDefinitionMapper::questionnaireDefinitionToDtoMapper);
    }

    public QuestionnaireDefinitionDto insertQuestionnaireDefinition(
            QuestionnaireDefinitionDto questionnaireDefinitionDto) {
        return questionnaireDefinitionMapper.questionnaireDefinitionToDtoMapper(
                    questionnaireDefinitionRepository.insert(
                    questionnaireDefinitionMapper.questionnaireDefinitionDtoToQuestionnaireDefinitionMapper(
                                                questionnaireDefinitionDto)));
    }

    public Optional<QuestionnaireDefinitionDto> updateQuestionnaireDefinitionBySymbol(
            String questionnaireDefinitionSymbol, QuestionnaireDefinitionDto questionnaireDefinitionDto) {
        return questionnaireDefinitionRepository.findBySymbol(questionnaireDefinitionSymbol)
                .map(existingProduct -> {
                    QuestionnaireDefinition questionnaireDefinition = questionnaireDefinitionMapper.questionnaireDefinitionDtoToQuestionnaireDefinitionMapper(questionnaireDefinitionDto);
                    questionnaireDefinitionRepository.save(questionnaireDefinition);
                    return questionnaireDefinitionMapper.questionnaireDefinitionToDtoMapper(questionnaireDefinition);
                });
    }

    public ResponseEntity<QuestionnaireDefinitionDto> publicate(
            String questionnaireDefinitionSymbol,
            PublicateQuestionnaireDefinitionDto publicateQuestionnaireDefinitionDto) {
        if (publicateQuestionnaireDefinitionDto.getPublicationDate().before(new Date())) {
            throw new DateNotMatchException("Data publikacji nie może być z przeszłości");
        }
        return null;
    }

    public Optional<QuestionnaireDefinitionDto> findPublicateQuestionnaireDefinition(Date now) {
        return Optional.ofNullable(now)
                .flatMap(questionnaireDefinitionRepository::findByExpiryDateIsNullAndPublicationDateIsNotNullAndPublicationDateGreaterThan)
                .map(questionnaireDefinitionMapper::questionnaireDefinitionToDtoMapper);
    }
}
