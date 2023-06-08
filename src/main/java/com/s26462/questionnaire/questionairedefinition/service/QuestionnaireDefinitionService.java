package com.s26462.questionnaire.questionairedefinition.service;

import com.s26462.questionnaire.exception.CannotModifyException;
import com.s26462.questionnaire.exception.DateNotMatchException;
import com.s26462.questionnaire.exception.EntityNotFoundException;
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

/**
 * The type Questionnaire definition service.
 */
@Service
@Log4j2
public class QuestionnaireDefinitionService {
    private final QuestionnaireDefinitionRepository questionnaireDefinitionRepository;
    private final QuestionnaireDefinitionMapper questionnaireDefinitionMapper;

    /**
     * Instantiates a new Questionnaire definition service.
     *
     * @param questionnaireDefinitionRepository the questionnaire definition repository
     * @param questionnaireDefinitionMapper     the questionnaire definition mapper
     */
    public QuestionnaireDefinitionService(
            QuestionnaireDefinitionRepository questionnaireDefinitionRepository,
            QuestionnaireDefinitionMapper questionnaireDefinitionMapper) {
        this.questionnaireDefinitionRepository = questionnaireDefinitionRepository;
        this.questionnaireDefinitionMapper = questionnaireDefinitionMapper;
    }

    /**
     * Gets questionnaires definitions.
     *
     * @return the questionnaires definitions
     */
    public List<QuestionnairesDefinitionsDto> getQuestionnairesDefinitions() {
        return questionnaireDefinitionRepository.findAll()
                .stream()
                .map(questionnaireDefinitionMapper::questionnairesDefinitionsToDtoMapper)
                .collect(Collectors.toList());
    }

    /**
     * Gets questionnaire definition by symbol.
     *
     * @param questionnaireDefinitionSymbol the questionnaire definition symbol
     * @return the questionnaire definition by symbol
     */
    public Optional<QuestionnaireDefinitionDto> getQuestionnaireDefinitionBySymbol(
            String questionnaireDefinitionSymbol) {
        return Optional.ofNullable(questionnaireDefinitionSymbol)
                .flatMap(questionnaireDefinitionRepository::findBySymbol)
                .map(questionnaireDefinitionMapper::questionnaireDefinitionToDtoMapper);
    }

    /**
     * Insert questionnaire definition questionnaire definition dto.
     *
     * @param questionnaireDefinitionDto the questionnaire definition dto
     * @return the questionnaire definition dto
     */
    public QuestionnaireDefinitionDto insertQuestionnaireDefinition(
            QuestionnaireDefinitionDto questionnaireDefinitionDto) {
        return questionnaireDefinitionMapper.questionnaireDefinitionToDtoMapper(
                    questionnaireDefinitionRepository.insert(
                    questionnaireDefinitionMapper.questionnaireDefinitionDtoToQuestionnaireDefinitionMapper(
                                                questionnaireDefinitionDto)));
    }

    /**
     * Update questionnaire definition by symbol optional.
     *
     * @param questionnaireDefinitionSymbol the questionnaire definition symbol
     * @param questionnaireDefinitionDto    the questionnaire definition dto
     * @return the optional
     */
    public Optional<QuestionnaireDefinitionDto> updateQuestionnaireDefinitionBySymbol(
            String questionnaireDefinitionSymbol, QuestionnaireDefinitionDto questionnaireDefinitionDto) {

        List<Optional<QuestionnaireDefinitionDto>> publicatedQuestionnaireList =
                findPublicateQuestionnaireDefinition(new Date());

        if(questionnaireDefinitionDto.getExpiryDate() != null || questionnaireDefinitionDto.getPublicationDate() != null) {
            throw new CannotModifyException("Niedozwolony sposób publikowania ankiety.");
        }

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

    /**
     * Publicate questionnaire definition optional.
     *
     * @param questionnaireDefinitionSymbol       the questionnaire definition symbol
     * @param publicateQuestionnaireDefinitionDto the publicate questionnaire definition dto
     * @return the optional
     */
    public Optional<QuestionnaireDefinitionDto> publicateQuestionnaireDefinition(
            String questionnaireDefinitionSymbol,
            PublicateQuestionnaireDefinitionDto publicateQuestionnaireDefinitionDto) {
        validatePublicationDate(publicateQuestionnaireDefinitionDto.getPublicationDate());

        Optional<QuestionnaireDefinitionDto> publicatedQuestionnaireDefinitionDto =
                getQuestionnaireDefinitionBySymbol(questionnaireDefinitionSymbol);
        publicatedQuestionnaireDefinitionDto.ifPresentOrElse(publicatedDto -> {
            if (publicatedDto.getPublicationDate() != null) {
                throw new FailToPublicateQuestionnaireDefinitionException("Wskazana ankieta jest już opublikowana.");
            }
            publicatedDto.setPublicationDate(publicateQuestionnaireDefinitionDto.getPublicationDate());
        }, () -> {
            throw new FailToPublicateQuestionnaireDefinitionException(
                    String.format("Nie znaleziono ankiety o symbolu %s", questionnaireDefinitionSymbol));
        });

        List<Optional<QuestionnaireDefinitionDto>> existingPublicateQuestionnaireDefinitionList =
                findPublicateQuestionnaireDefinition(new Date());

        updateExpiryDates(existingPublicateQuestionnaireDefinitionList, publicateQuestionnaireDefinitionDto);

        try {
            updatePublicatedQuestionnaires(publicatedQuestionnaireDefinitionDto);
            updateExistingPublicatedQuestionnaires(existingPublicateQuestionnaireDefinitionList);

        } catch (FailToPublicateQuestionnaireDefinitionException ex) {
            throw new FailToPublicateQuestionnaireDefinitionException(
                    String.format("Nie udało się opublikować ankiety o symbolu %s", questionnaireDefinitionSymbol));
        }

        return publicatedQuestionnaireDefinitionDto;
    }

    private void updateExpiryDates(List<Optional<QuestionnaireDefinitionDto>> questionnaireDefinitionList,
                                   PublicateQuestionnaireDefinitionDto publicateQuestionnaireDefinitionDto) {
        questionnaireDefinitionList.forEach(existingPublicateQuestionnaireDefinition ->
                existingPublicateQuestionnaireDefinition.ifPresent(existingDto -> {
                    if (existingDto.getExpiryDate().compareTo(new Date()) > 0) {
                        existingDto.setExpiryDate(publicateQuestionnaireDefinitionDto.getPublicationDate());
                    }
                }));
    }

    private void updatePublicatedQuestionnaires(Optional<QuestionnaireDefinitionDto> publicatedQuestionnaireDefinitionDto) {
        publicatedQuestionnaireDefinitionDto.flatMap(publicatedDto ->
                questionnaireDefinitionRepository.findBySymbol(publicatedDto.getSymbol())
                        .flatMap(existingQuestionnaire -> updateQuestionnaireDefinition(existingQuestionnaire, publicatedDto))
        );
    }

    private void updateExistingPublicatedQuestionnaires(List<Optional<QuestionnaireDefinitionDto>> questionnaireDefinitionList) {
        questionnaireDefinitionList.stream()
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


    /**
     * Delete questionnaire definition by symbol.
     *
     * @param questionnaireDefinitionSymbol the questionnaire definition symbol
     */
    public void deleteQuestionnaireDefinitionBySymbol(String questionnaireDefinitionSymbol) {
        questionnaireDefinitionRepository.findBySymbol(questionnaireDefinitionSymbol)
                .ifPresentOrElse(questionnaireDefinition -> {
                    if (questionnaireDefinition.getPublicationDate() != null) {
                        throw new CannotModifyException("Nie można usunąć już raz opublikowanej ankiety.");
                    }
                    questionnaireDefinitionRepository.delete(questionnaireDefinition);
                }, () -> {
                    throw new EntityNotFoundException(
                            String.format("Nie znaleziono ankiety o symbolu %s", questionnaireDefinitionSymbol));
                });
    }

}
