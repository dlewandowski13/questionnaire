package com.s26462.questionnaire.questionnaire.controller;

import com.s26462.questionnaire.questionnaire.dto.QuestionnaireDto;
import com.s26462.questionnaire.questionnaire.service.QuestionnaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
