package com.s26462.questionnaire.questionnaire.service;

import com.s26462.questionnaire.product.Product;
import com.s26462.questionnaire.product.ProductRepository;
import com.s26462.questionnaire.questionnaire.dto.AnswerDto;
import com.s26462.questionnaire.questionnaire.dto.QuestionDto;
import com.s26462.questionnaire.questionnaire.dto.QuestionnaireDto;
import com.s26462.questionnaire.questionnaire.dto.QuestionnaireWithProductsDto;
import com.s26462.questionnaire.questionnaire.mapper.QuestionnaireMapper;
import com.s26462.questionnaire.questionnaire.repository.QuestionnaireRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class QuestionnaireService {

    private final QuestionnaireMapper questionnaireMapper;
    private final QuestionnaireRepository questionnaireRepository;
    private final ProductRepository productRepository;

    public QuestionnaireService(QuestionnaireMapper questionnaireMapper, QuestionnaireRepository questionnaireRepository, ProductRepository productRepository) {
        this.questionnaireMapper = questionnaireMapper;
        this.questionnaireRepository = questionnaireRepository;
        this.productRepository = productRepository;
    }

    public QuestionnaireWithProductsDto insertQuestionnaire(QuestionnaireDto questionnaireDto) {
        QuestionnaireWithProductsDto questionnaireWithProductsDto = designateProducts(questionnaireDto);
        questionnaireRepository.insert(
                questionnaireMapper.questionnaireWithProductsDtoToQuestionnaireMapper(
                        questionnaireWithProductsDto));
        return questionnaireWithProductsDto;
    }

    private QuestionnaireWithProductsDto designateProducts(QuestionnaireDto questionnaireDto) {
        List<QuestionDto> questions = questionnaireDto.getQuestions();
        List<Product> products = productRepository.findAll();
        List<String> eliminatedProducts = getEliminatedProducts(questions);

        List<Product> productsToRemove = products.stream()
                .filter(product -> eliminatedProducts.contains(product.getName()))
                .toList();

        products.removeAll(productsToRemove);

        QuestionnaireWithProductsDto questionnaireWithProductsDto = questionnaireMapper
                .mapToQuestionnaireWithProductsDto(questionnaireDto);
        questionnaireWithProductsDto.setProducts(products.stream()
                .map(Product::getName)
                .toList());

        return questionnaireWithProductsDto;
    }



    private List<String> getEliminatedProducts(List<QuestionDto> questions) {
        return questions.stream()
                .map(QuestionDto::getAnswer)
                .filter(Objects::nonNull)
                .flatMap(answer -> answer.values().stream().findFirst().stream())
                .filter(answerDto -> answerDto.getEliminatedProducts() != null)
                .flatMap(answerDto -> answerDto.getEliminatedProducts().stream())
                .toList();
    }


}
