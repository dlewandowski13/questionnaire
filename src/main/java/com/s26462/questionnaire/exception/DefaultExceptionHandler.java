package com.s26462.questionnaire.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The type Default exception handler.
 */
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle duplicate key exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException ex, WebRequest request) {
        StringBuilder body = new StringBuilder();
        body.append("Zduplikowane wartości.\n")
                .append("Message: ")
                .append(ex.getMessage());
        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Handle entity not found exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        StringBuilder body = new StringBuilder();
        body.append("Nie znaleziono wartości o podanym identyfikatorze.\n")
                .append("Message: ")
                .append(ex.getMessage());
        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DateNotMatchException.class)
    public ResponseEntity<Object> handleDateNotMatchException(DateNotMatchException ex, WebRequest request) {
        StringBuilder body = new StringBuilder();
        body.append("Nieprawidłowa data.\n")
                .append("Message: ")
                .append(ex.getMessage());
        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(FailToPublicateQuestionnaireDefinitionException.class)
    public ResponseEntity<Object> handleFailToBublicQuestionnaireDefinitionException(
            FailToPublicateQuestionnaireDefinitionException ex, WebRequest request) {
        StringBuilder body = new StringBuilder();
        body.append("Nie udało się opublikować ankiety.\n")
                .append("Message: ")
                .append(ex.getMessage());
        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(CannotModifyException.class)
    public ResponseEntity<Object> handleCannotModifyQuestionnaireDefinitionException(
            CannotModifyException ex, WebRequest request) {
        StringBuilder body = new StringBuilder();
        body.append("Nie udało się opublikować ankiety.\n")
                .append("Message: ")
                .append(ex.getMessage());
        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}
