package com.s26462.questionnaire.questionnaire.mapper;

import com.s26462.questionnaire.questionnaire.collection.Questionnaire;
import com.s26462.questionnaire.questionnaire.collection.utils.Answer;
import com.s26462.questionnaire.questionnaire.collection.utils.Question;
import com.s26462.questionnaire.questionnaire.dto.AnswerDto;
import com.s26462.questionnaire.questionnaire.dto.QuestionDto;
import com.s26462.questionnaire.questionnaire.dto.QuestionnaireDto;
import com.s26462.questionnaire.questionnaire.dto.QuestionnaireWithProductsDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Questionnaire mapper.
 */
@Component
public class QuestionnaireMapper {
    private final ModelMapper modelMapper;

    /**
     * Instantiates a new Questionnaire mapper.
     *
     * @param modelMapper the model mapper
     */
    public QuestionnaireMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Map to questionnaire with products dto questionnaire with products dto.
     *
     * @param questionnaireDto the questionnaire dto
     * @return the questionnaire with products dto
     */
    public QuestionnaireWithProductsDto mapToQuestionnaireWithProductsDto(QuestionnaireDto questionnaireDto) {
        return modelMapper.map(questionnaireDto, QuestionnaireWithProductsDto.class);
    }

    /**
     * Questionnaire to questionnaire with products dto mapper questionnaire with products dto.
     *
     * @param questionnaire the questionnaire
     * @return the questionnaire with products dto
     */
    public QuestionnaireWithProductsDto questionnaireToQuestionnaireWithProductsDtoMapper(Questionnaire questionnaire) {
        QuestionnaireWithProductsDto questionnaireWithProductsDtoDto
                = modelMapper.map(questionnaire, QuestionnaireWithProductsDto.class);

        List<Question> questions = questionnaire.getQuestions();
        List<QuestionDto> questionDtos = questions.stream()
                .map(this::mapQuestionToQuestionDto)
                .collect(Collectors.toList());

        questionnaireWithProductsDtoDto.setQuestions(questionDtos);
        return questionnaireWithProductsDtoDto;
    }

    /**
     * Questionnaire with products dto to questionnaire mapper questionnaire.
     *
     * @param questionnaireWithProductsDto the questionnaire with products dto
     * @return the questionnaire
     */
    public Questionnaire questionnaireWithProductsDtoToQuestionnaireMapper(QuestionnaireWithProductsDto questionnaireWithProductsDto) {
        Questionnaire questionnaire = modelMapper.map(questionnaireWithProductsDto, Questionnaire.class);

        List<QuestionDto> questionDtos = questionnaireWithProductsDto.getQuestions();
        List<Question> questions = questionDtos.stream()
                .map(this::mapQuestionDtoToQuestion)
                .collect(Collectors.toList());

        questionnaire.setQuestions(questions);
        return questionnaire;
    }

    private Question mapQuestionDtoToQuestion(QuestionDto questionDto) {
        Question question = new Question();
        question.setQuestion(questionDto.getQuestion());

        LinkedHashMap<String, AnswerDto> answerDtos = questionDto.getAnswer();
        LinkedHashMap<String, Answer> answers = answerDtos.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry ->
                        mapAnswerDtoToAnswer(entry.getValue()), (a, b) -> a, LinkedHashMap::new));

        question.setAnswer(answers);
        return question;
    }

    private Answer mapAnswerDtoToAnswer(AnswerDto answerDto) {
        Answer answer = new Answer();
        answer.setDescription(answerDto.getDescription());
        answer.setEliminatedProducts(answerDto.getEliminatedProducts());
        return answer;
    }

    private QuestionDto mapQuestionToQuestionDto(Question question) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setQuestion(question.getQuestion());

        LinkedHashMap<String, Answer> answers = question.getAnswer();
        LinkedHashMap<String, AnswerDto> answerDtos = answers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> mapAnswerToAnswerDto(entry.getValue()), (a, b) -> a, LinkedHashMap::new));

        questionDto.setAnswer(answerDtos);
        return questionDto;
    }

    private AnswerDto mapAnswerToAnswerDto(Answer answer) {
        AnswerDto answerDto = new AnswerDto();
        answerDto.setDescription(answer.getDescription());
        answerDto.setEliminatedProducts(answer.getEliminatedProducts());
        return answerDto;
    }
}
