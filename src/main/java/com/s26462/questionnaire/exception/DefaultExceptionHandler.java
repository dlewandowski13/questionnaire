package com.s26462.questionnaire.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The type Default exception handler.
 */
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handle duplicate key exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
        ErrorResponseImpl errorResponse = new ErrorResponseImpl(HttpStatus.CONFLICT, "Podany symbol już istnieje.", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
