package com.s26462.questionnaire.auth;

import com.s26462.questionnaire.configuration.JwtService;
import com.s26462.questionnaire.users.Role;
import com.s26462.questionnaire.users.User;
import com.s26462.questionnaire.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serwis służący do obsługi modułu autentykacji
 *
 * @author dawid
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Obsługa rejestracji użytkownika.
     *
     * @param reqisterRequest żądanie rejestracji
     * @return wygenerowany token autoryzacyjny
     */
    public AuthenticationResponse register(ReqisterRequest reqisterRequest) {
        var user = User.builder()
                .username(reqisterRequest.getUsername())
                .password(passwordEncoder.encode(reqisterRequest.getPassword()))
                .role(Role.USER)
                .build();
        var jwtToken = jwtService.generateToken(user);
        userRepository.save(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Obsługa autoryzacji użytkownika.
     *
     * @param authenticationRequest żądanie rejestracji
     * @return wygenerowany token autoryzacyjny
     */
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow();
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }
}
