package com.s26462.questionnaire.questionairedefinition.mapper;

import com.s26462.questionnaire.questionairedefinition.collection.QuestionnaireDefinition;
import com.s26462.questionnaire.questionairedefinition.collection.utils.Answer;
import com.s26462.questionnaire.questionairedefinition.collection.utils.Question;
import com.s26462.questionnaire.questionairedefinition.dto.AnswerDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionDto;
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

//    public QuestionnaireDefinitionDto questionnaireDefinitionToDtoMapper(
//            QuestionnaireDefinition questionnaireDefinition) {
//        return modelMapper.map(questionnaireDefinition, QuestionnaireDefinitionDto.class);
//    }

//    public QuestionnaireDefinition questionnaireDefinitionDtoToQuestionnaireDefinitionMapper(
//            QuestionnaireDefinitionDto questionnaireDefinitionDto) {
//        return modelMapper.map(questionnaireDefinitionDto, QuestionnaireDefinition.class);
//    }

    public QuestionnairesDefinitionsDto questionnairesDefinitionsToDtoMapper(QuestionnaireDefinition questionnaireDefinition) {
        return modelMapper.map(questionnaireDefinition, QuestionnairesDefinitionsDto.class);
    }

    public QuestionnaireDefinitionDto questionnaireDefinitionToDtoMapper(QuestionnaireDefinition questionnaireDefinition) {
        QuestionnaireDefinitionDto questionnaireDefinitionDto = modelMapper.map(questionnaireDefinition, QuestionnaireDefinitionDto.class);

        List<Question> questions = questionnaireDefinition.getQuestions();
        List<QuestionDto> questionDtos = questions.stream()
                .map(this::mapQuestionToQuestionDto)
                .collect(Collectors.toList());

        questionnaireDefinitionDto.setQuestions(questionDtos);
        return questionnaireDefinitionDto;
    }

    public QuestionnaireDefinition questionnaireDefinitionDtoToQuestionnaireDefinitionMapper(QuestionnaireDefinitionDto questionnaireDefinitionDto) {
        QuestionnaireDefinition questionnaireDefinition = modelMapper.map(questionnaireDefinitionDto, QuestionnaireDefinition.class);

        List<QuestionDto> questionDtos = questionnaireDefinitionDto.getQuestions();
        List<Question> questions = questionDtos.stream()
                .map(this::mapQuestionDtoToQuestion)
                .collect(Collectors.toList());

        questionnaireDefinition.setQuestions(questions);
        return questionnaireDefinition;
    }

    private Question mapQuestionDtoToQuestion(QuestionDto questionDto) {
        Question question = new Question();
        question.setQuestion(questionDto.getQuestion());

        LinkedHashMap<String, AnswerDto> answerDtos = questionDto.getAnswerDefinition();
        LinkedHashMap<String, Answer> answers = answerDtos.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> mapAnswerDtoToAnswer(entry.getValue()), (a, b) -> a, LinkedHashMap::new));

        question.setAnswerDefinition(answers);
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

        LinkedHashMap<String, Answer> answers = question.getAnswerDefinition();
        LinkedHashMap<String, AnswerDto> answerDtos = answers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> mapAnswerToAnswerDto(entry.getValue()), (a, b) -> a, LinkedHashMap::new));

        questionDto.setAnswerDefinition(answerDtos);
        return questionDto;
    }

    private AnswerDto mapAnswerToAnswerDto(Answer answer) {
        AnswerDto answerDto = new AnswerDto();
        answerDto.setDescription(answer.getDescription());
        answerDto.setEliminatedProducts(answer.getEliminatedProducts());
        return answerDto;
    }

}
