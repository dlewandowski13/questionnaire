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
import java.util.Collections;
import java.util.stream.Stream;

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

        List<Optional<QuestionnaireDefinitionDto>> publicatedQuestionnaireList =
                findPublicateQuestionnaireDefinition(new Date());

        boolean isPublicated =
                publicatedQuestionnaireList.stream()
                        .anyMatch(publicatedQuestionnaire ->
                                publicatedQuestionnaire.map(QuestionnaireDefinitionDto::getSymbol)
                                        .filter(symbol -> symbol.equals(questionnaireDefinitionSymbol))
                                        .isPresent());

        if (isPublicated) {
            throw new CannotModifyException("Nie można edytować opublikowanej ankiety");
        }

        return questionnaireDefinitionRepository.findBySymbol(questionnaireDefinitionSymbol)
                .flatMap(existingQuestionnaire ->
                        updateQuestionnaireDefinition(existingQuestionnaire, questionnaireDefinitionDto));
    }


    public Optional<QuestionnaireDefinitionDto> publicateQuestionnaireDefinition(
            String questionnaireDefinitionSymbol,
            PublicateQuestionnaireDefinitionDto publicateQuestionnaireDefinitionDto) {
        validatePublicationDate(publicateQuestionnaireDefinitionDto.getPublicationDate());

        Optional<QuestionnaireDefinitionDto> publicatedQuestionnaireDefinitionDto =
                getQuestionnaireDefinitionBySymbol(questionnaireDefinitionSymbol);
        publicatedQuestionnaireDefinitionDto.ifPresent(publicatedDto -> {
            if (publicatedDto.getPublicationDate() != null) {
                throw new FailToPublicateQuestionnaireDefinitionException("Wskazana ankieta jest już opublikowana.");
            }
            publicatedDto.setPublicationDate(publicateQuestionnaireDefinitionDto.getPublicationDate());
        });

        List<Optional<QuestionnaireDefinitionDto>> existingPublicateQuestionnaireDefinitionList =
                findPublicateQuestionnaireDefinition(new Date());

        existingPublicateQuestionnaireDefinitionList.forEach(existingPublicateQuestionnaireDefinition ->
                existingPublicateQuestionnaireDefinition.ifPresent(existingDto ->
                existingDto.setExpiryDate(publicateQuestionnaireDefinitionDto.getPublicationDate())));

        try {
            publicatedQuestionnaireDefinitionDto.flatMap(publicatedDto ->
                    questionnaireDefinitionRepository.findBySymbol(publicatedDto.getSymbol())
                            .flatMap(existingQuestionnaire -> updateQuestionnaireDefinition(existingQuestionnaire, publicatedDto))
            );

            existingPublicateQuestionnaireDefinitionList.stream()
                    .flatMap(existingDtoOptional ->
                            existingDtoOptional
                                    .filter(existingDto -> existingDto.getExpiryDate().compareTo(new Date()) > 0)
                                    .map(existingDto ->
                                            questionnaireDefinitionRepository.findBySymbol(existingDto.getSymbol())
                                                    .flatMap(publicatedQuestionnaire -> updateQuestionnaireDefinition(publicatedQuestionnaire, existingDto))
                                                    .stream()
                                    )
                                    .orElseGet(Stream::empty)
                    )
                    .forEach(result -> {});


        } catch (FailToPublicateQuestionnaireDefinitionException ex) {
            throw new FailToPublicateQuestionnaireDefinitionException(
                    String.format("Nie udało się opublikować ankiety o symbolu %s", questionnaireDefinitionSymbol));
        }

        return publicatedQuestionnaireDefinitionDto;
    }

    private List<Optional<QuestionnaireDefinitionDto>> findPublicateQuestionnaireDefinition(Date now) {
        return Optional.ofNullable(now)
                .map(date -> questionnaireDefinitionRepository.findCustomQuestionnaireDefinition(date)
                        .stream()
                        .map(optional -> optional.map(questionnaireDefinitionMapper::questionnaireDefinitionToDtoMapper))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private void validatePublicationDate(Date publicationDate) {
        if (publicationDate.before(new Date())) {
            throw new DateNotMatchException("Data publikacji nie może być z przeszłości");
        }
    }

    private Optional<QuestionnaireDefinitionDto> updateQuestionnaireDefinition(QuestionnaireDefinition existingQuestionnaire,
                                                                               QuestionnaireDefinitionDto questionnaireDefinitionDto) {
        QuestionnaireDefinition updatedQuestionnaire =
                questionnaireDefinitionMapper.questionnaireDefinitionDtoToQuestionnaireDefinitionMapper(questionnaireDefinitionDto);
        updatedQuestionnaire.setId(existingQuestionnaire.getId());
        questionnaireDefinitionRepository.save(updatedQuestionnaire);
        return Optional.of(questionnaireDefinitionMapper.questionnaireDefinitionToDtoMapper(updatedQuestionnaire));
    }


}
