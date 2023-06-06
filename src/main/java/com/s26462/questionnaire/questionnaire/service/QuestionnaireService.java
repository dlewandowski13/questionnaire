package com.s26462.questionnaire.questionnaire.service;

import com.s26462.questionnaire.questionnaire.dto.QuestionnaireDto;
import com.s26462.questionnaire.questionnaire.mapper.QuestionnaireMapper;
import com.s26462.questionnaire.questionnaire.repository.QuestionnaireRepository;
import org.springframework.stereotype.Service;


@Service
public class QuestionnaireService {

    private final QuestionnaireMapper questionnaireMapper;
    private final QuestionnaireRepository questionnaireRepository;

    public QuestionnaireService(QuestionnaireMapper questionnaireMapper, QuestionnaireRepository questionnaireRepository) {
        this.questionnaireMapper = questionnaireMapper;
        this.questionnaireRepository = questionnaireRepository;
    }

    public QuestionnaireDto insertQuestionnaire(QuestionnaireDto questionnaireDto) {
        return questionnaireMapper.questionnaireToDtoMapper(
                questionnaireRepository.insert(
                        questionnaireMapper.questionnaireDtoToQuestionnaireMapper(
                                questionnaireDto)));
    }
}
