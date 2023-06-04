package com.s26462.questionnaire.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

/**
 * The type Error response.
 */
public class ErrorResponseImpl implements ErrorResponse {
    /**
     * Instantiates a new Error response.
     *
     * @param httpStatus the http status
     * @param s          the s
     * @param message    the message
     */
    public ErrorResponseImpl(HttpStatus httpStatus, String s, String message) {
    }
    @Override
    public HttpStatusCode getStatusCode() {
        return null;
    }

    @Override
    public ProblemDetail getBody() {
        return null;
    }
}
