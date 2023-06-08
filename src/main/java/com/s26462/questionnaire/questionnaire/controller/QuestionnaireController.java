package com.s26462.questionnaire.questionnaire.controller;

import com.s26462.questionnaire.questionnaire.dto.QuestionnaireDto;
import com.s26462.questionnaire.questionnaire.dto.QuestionnaireWithProductsDto;
import com.s26462.questionnaire.questionnaire.service.QuestionnaireService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * The type Questionnaire controller.
 */
@Controller
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    /**
     * Instantiates a new Questionnaire controller.
     *
     * @param questionnaireService the questionnaire service
     */
    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    /**
     * Post questionnaire response entity.
     *
     * @param questionnaireDto the questionnaire dto
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<Object> postQuestionnaire(
            @RequestBody QuestionnaireDto questionnaireDto) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{questionnaireId}")
                .buildAndExpand(questionnaireService.insertQuestionnaire(questionnaireDto).getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Gets questionnaire.
     *
     * @param questionnaireId the questionnaire id
     * @return the questionnaire
     */
    @GetMapping("/{questionnaireId}")
    public ResponseEntity<QuestionnaireWithProductsDto> getQuestionnaire(
            @PathVariable String questionnaireId) {
        return questionnaireService.getQuestionnaireById(questionnaireId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Gets questionnaire pdf.
     *
     * @param questionnaireId the questionnaire id
     * @return the questionnaire pdf
     */
    @GetMapping("/{questionnaireId}/pdf")
    public ResponseEntity<byte[]> getQuestionnairePdf(@PathVariable String questionnaireId) {
        byte[] pdfBytes = questionnaireService.getQuestionnairePdfById(questionnaireId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "questionnaire.pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);

        return pdfBytes != null
                ? ResponseEntity.ok().headers(headers).body(pdfBytes)
                : ResponseEntity.badRequest().build();
    }
}
