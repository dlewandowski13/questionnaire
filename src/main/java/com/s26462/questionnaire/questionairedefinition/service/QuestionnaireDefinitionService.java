package com.s26462.questionnaire.questionairedefinition.service;

import com.s26462.questionnaire.exception.CannotModifyException;
import com.s26462.questionnaire.exception.DateNotMatchException;
import com.s26462.questionnaire.exception.FailToPublicateQuestionnaireDefinitionException;
import com.s26462.questionnaire.questionairedefinition.collection.QuestionnaireDefinition;
import com.s26462.questionnaire.questionairedefinition.dto.PublicateQuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnairesDefinitionsDto;
import com.s26462.questionnaire.questionairedefinition.mapper.QuestionnaireDefinitionMapper;
import com.s26462.questionnaire.questionairedefinition.repository.QuestionnaireDefinitionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
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
        findPublicateQuestionnaireDefinition(new Date())
                .filter(publicatedQuestionnaire -> publicatedQuestionnaire.getSymbol().equals(questionnaireDefinitionSymbol))
                .ifPresent(publicatedQuestionnaire -> {
                    throw new CannotModifyException("Nie można edytować opublikowanej ankiety");
                });
        return questionnaireDefinitionRepository.findBySymbol(questionnaireDefinitionSymbol)
                .map(existingQuestionnaire -> {
                    QuestionnaireDefinition questionnaireDefinition =
                            questionnaireDefinitionMapper.questionnaireDefinitionDtoToQuestionnaireDefinitionMapper(
                                    questionnaireDefinitionDto);
                    questionnaireDefinitionRepository.save(questionnaireDefinition);
                    return questionnaireDefinitionMapper.questionnaireDefinitionToDtoMapper(questionnaireDefinition);
                });
    }

    public Optional<QuestionnaireDefinitionDto> publicateQuestionnaireDefinition(String questionnaireDefinitionSymbol,
                                                                                 PublicateQuestionnaireDefinitionDto publicateQuestionnaireDefinitionDto) {
        validatePublicationDate(publicateQuestionnaireDefinitionDto.getPublicationDate());

        Optional<QuestionnaireDefinitionDto> publicatedQuestionnaireDefinitionDto =
                getQuestionnaireDefinitionBySymbol(questionnaireDefinitionSymbol);

        Optional<QuestionnaireDefinitionDto> existingPublicateQuestionnaireDefinition =
                validateExistingPublicateQuestionnaireDefinition();

        try {
            existingPublicateQuestionnaireDefinition.ifPresent(questionnaireDefinitionDto ->
                    questionnaireDefinitionDto.setExpiryDate(publicateQuestionnaireDefinitionDto.getPublicationDate()));
            publicatedQuestionnaireDefinitionDto.ifPresent(questionnaireDefinitionDto ->
                    questionnaireDefinitionDto.setPublicationDate(publicateQuestionnaireDefinitionDto.getPublicationDate()));
        } catch (FailToPublicateQuestionnaireDefinitionException ex) {
            throw new FailToPublicateQuestionnaireDefinitionException(
                    String.format("Nie udało się opublikować ankiety o symbolu %s", questionnaireDefinitionSymbol));
        }

        return publicatedQuestionnaireDefinitionDto;
    }

    public Optional<QuestionnaireDefinitionDto> findPublicateQuestionnaireDefinition(Date now) {
        return Optional.ofNullable(now)
                .flatMap(questionnaireDefinitionRepository::findByExpiryDateIsNullAndPublicationDateIsNotNullAndPublicationDateGreaterThan)
                .map(questionnaireDefinitionMapper::questionnaireDefinitionToDtoMapper);
    }

    public Optional<QuestionnaireDefinitionDto> validateExistingPublicateQuestionnaireDefinition(){
        log.info("validateExistingPublicateQuestionnaireDefinition: " + findPublicateQuestionnaireDefinition(new Date()));
        return findPublicateQuestionnaireDefinition(new Date());
    }

    public void validatePublicationDate(Date publicationDate) {
        if (publicationDate.before(new Date())) {
            throw new DateNotMatchException("Data publikacji nie może być z przeszłości");
        }
    }
}
