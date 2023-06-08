package com.s26462.questionnaire.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler obsługujący rejestrację i autoryzację użytkownika.
 *
 * @author dawid
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Rejestracja nowego użytkownika.
     *
     * @param reqisterRequest dane wejściowe
     * @return token dostępowy dla nowego użytkownika
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody ReqisterRequest reqisterRequest
    ) {
        return ResponseEntity.ok(authenticationService.register(reqisterRequest));
    }

    /**
     * Autoryzacja użytkownika
     *
     * @param authenticationRequest dane wejściowe
     * @return token dostępowy dla nowego użytkownika
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
}
