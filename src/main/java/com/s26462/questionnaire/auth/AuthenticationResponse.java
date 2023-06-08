package com.s26462.questionnaire.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klasa reprezentująca strukturę odpowiedzi
 * na żądanie autoryzaci i rejestracji.
 *
 * @author dawid
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
}
