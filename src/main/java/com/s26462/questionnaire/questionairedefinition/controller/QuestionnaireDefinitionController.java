package com.s26462.questionnaire.questionairedefinition.controller;

import com.s26462.questionnaire.questionairedefinition.dto.PublicateQuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnaireDefinitionDto;
import com.s26462.questionnaire.questionairedefinition.dto.QuestionnairesDefinitionsDto;
import com.s26462.questionnaire.questionairedefinition.service.QuestionnaireDefinitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * The type Questionnaire definition controller.
 */
@Controller
@RequestMapping("/questionnaireDefinition")
public class QuestionnaireDefinitionController {

    private final QuestionnaireDefinitionService questionnaireDefinitionService;

    /**
     * Instantiates a new Questionnaire definition controller.
     *
     * @param questionnaireDefinitionService the questionnaire definition service
     */
    public QuestionnaireDefinitionController(QuestionnaireDefinitionService questionnaireDefinitionService) {
        this.questionnaireDefinitionService = questionnaireDefinitionService;
    }

    /**
     * Post questionnaire definition response entity.
     *
     * @param questionnaireDefinitionDto the questionnaire definition dto
     * @return the response entity
     */
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

    /**
     * Gets questionnaires definitions.
     *
     * @return the questionnaires definitions
     */
    @GetMapping("/list")
    public ResponseEntity<List<QuestionnairesDefinitionsDto>> getQuestionnairesDefinitions() {
        return ResponseEntity.ok(questionnaireDefinitionService.getQuestionnairesDefinitions());
    }

    /**
     * Gets questionnaire definition by symbol.
     *
     * @param questionnaireDefinitionSymbol the questionnaire definition symbol
     * @return the questionnaire definition by symbol
     */
    @GetMapping("/{questionnaireDefinitionSymbol}")
    public ResponseEntity<QuestionnaireDefinitionDto> getQuestionnaireDefinitionBySymbol(
            @PathVariable String questionnaireDefinitionSymbol) {
        return questionnaireDefinitionService.getQuestionnaireDefinitionBySymbol(questionnaireDefinitionSymbol)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update questionnaire definition by symbol response entity.
     *
     * @param questionnaireDefinitionSymbol the questionnaire definition symbol
     * @param questionnaireDefinitionDto    the questionnaire definition dto
     * @return the response entity
     */
    @PutMapping("/{questionnaireDefinitionSymbol}")
    public ResponseEntity<QuestionnaireDefinitionDto> updateQuestionnaireDefinitionBySymbol(
            @PathVariable String questionnaireDefinitionSymbol,
            @RequestBody QuestionnaireDefinitionDto questionnaireDefinitionDto){
        return questionnaireDefinitionService
                .updateQuestionnaireDefinitionBySymbol(questionnaireDefinitionSymbol, questionnaireDefinitionDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Public questionnaire definition by symbol response entity.
     *
     * @param questionnaireDefinitionSymbol       the questionnaire definition symbol
     * @param publicateQuestionnaireDefinitionDto the publicate questionnaire definition dto
     * @return the response entity
     */
    @PatchMapping("/public/{questionnaireDefinitionSymbol}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<QuestionnaireDefinitionDto> publicQuestionnaireDefinitionBySymbol(
            @PathVariable String questionnaireDefinitionSymbol,
            @RequestBody PublicateQuestionnaireDefinitionDto publicateQuestionnaireDefinitionDto) {
        return questionnaireDefinitionService.publicateQuestionnaireDefinition(questionnaireDefinitionSymbol, publicateQuestionnaireDefinitionDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete questionnaire definition by symbol response entity.
     *
     * @param questionnaireDefinitionSymbol the questionnaire definition symbol
     * @return the response entity
     */
    @DeleteMapping("/{questionnaireDefinitionSymbol}")
    public ResponseEntity<Void> deleteQuestionnaireDefinitionBySymbol(
            @PathVariable String questionnaireDefinitionSymbol) {
            questionnaireDefinitionService.deleteQuestionnaireDefinitionBySymbol(questionnaireDefinitionSymbol);
            return ResponseEntity.noContent().build();
    }

}
