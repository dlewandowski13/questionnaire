package com.s26462.questionnaire.questionairedefinition.controller;

import com.s26462.questionnaire.questionairedefinition.dto.PublicateQuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnairesDefinitionsDto;
import com.s26462.questionnaire.questionairedefinition.service.QuestionnaireDefinitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/questionnaireDefinition")
public class QuestionnaireDefinitionController {

    private final QuestionnaireDefinitionService questionnaireDefinitionService;

    public QuestionnaireDefinitionController(QuestionnaireDefinitionService questionnaireDefinitionService) {
        this.questionnaireDefinitionService = questionnaireDefinitionService;
    }

    @PostMapping
    public ResponseEntity<Object> postQuestionnaireDefinition(
            @RequestBody QuestionnaireDefinitionDto questionnaireDefinitionDto) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{questionnaireDefinitionSymbol}")
                .buildAndExpand(questionnaireDefinitionService
                        .insertQuestionnaireDefinition(questionnaireDefinitionDto).getSymbol())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<QuestionnairesDefinitionsDto>> getQuestionnairesDefinitions() {
        return ResponseEntity.ok(questionnaireDefinitionService.getQuestionnairesDefinitions());
    }

    @GetMapping("/{questionnaireDefinitionSymbol}")
    public ResponseEntity<QuestionnaireDefinitionDto> getQuestionnaireDefinitionBySymbol(
            @PathVariable String questionnaireDefinitionSymbol) {
        return questionnaireDefinitionService.getQuestionnaireDefinitionBySymbol(questionnaireDefinitionSymbol)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{questionnaireDefinitionSymbol}")
    public ResponseEntity<QuestionnaireDefinitionDto> updateQuestionnaireDefinitionBySymbol(
            @PathVariable String questionnaireDefinitionSymbol,
            @RequestBody QuestionnaireDefinitionDto questionnaireDefinitionDto){
        return questionnaireDefinitionService
                .updateQuestionnaireDefinitionBySymbol(questionnaireDefinitionSymbol, questionnaireDefinitionDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/public/{questionnaireDefinitionSymbol}")
    public ResponseEntity<QuestionnaireDefinitionDto> publicQuestionnaireDefinitionBySymbol(
            @PathVariable String questionnaireDefinitionSymbol,
            @RequestBody PublicateQuestionnaireDefinitionDto publicateQuestionnaireDefinitionDto) {
        return questionnaireDefinitionService.publicateQuestionnaireDefinition(questionnaireDefinitionSymbol, publicateQuestionnaireDefinitionDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
