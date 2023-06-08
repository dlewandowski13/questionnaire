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

@Controller
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

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

    @GetMapping("/{questionnaireId}")
    public ResponseEntity<QuestionnaireWithProductsDto> getQuestionnaire(
            @PathVariable String questionnaireId) {
        return questionnaireService.getQuestionnaireById(questionnaireId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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
