package com.s26462.questionnaire.questionnaire.service;

import com.s26462.questionnaire.product.Product;
import com.s26462.questionnaire.product.ProductRepository;
import com.s26462.questionnaire.questionnaire.dto.QuestionDto;
import com.s26462.questionnaire.questionnaire.dto.QuestionnaireDto;
import com.s26462.questionnaire.questionnaire.dto.QuestionnaireWithProductsDto;
import com.s26462.questionnaire.questionnaire.mapper.QuestionnaireMapper;
import com.s26462.questionnaire.questionnaire.pdfgenerator.PdfGenerator;
import com.s26462.questionnaire.questionnaire.repository.QuestionnaireRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        return questionnaireMapper.questionnaireToQuestionnaireWithProductsDtoMapper(questionnaireRepository.insert(
                questionnaireMapper.questionnaireWithProductsDtoToQuestionnaireMapper(
                        designateProducts(questionnaireDto))));
    }

    public Optional<QuestionnaireWithProductsDto> getQuestionnaireById(String questionnaireId) {
        return Optional.ofNullable(questionnaireId)
                .flatMap(questionnaireRepository::findById)
                .map(questionnaireMapper::questionnaireToQuestionnaireWithProductsDtoMapper);
    }

    private QuestionnaireWithProductsDto designateProducts(QuestionnaireDto questionnaireDto) {
        List<QuestionDto> questions = questionnaireDto.getQuestions();
        List<Product> products = productRepository.findAllActiveProducts();
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


    public byte[] getQuestionnairePdfById(String questionnaireId) {
        return getQuestionnaireById(questionnaireId)
                .map(PdfGenerator::generatePdfFromQuestionnaire)
                .orElseThrow();
    }
}
