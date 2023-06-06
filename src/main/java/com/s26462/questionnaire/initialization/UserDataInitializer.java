package com.s26462.questionnaire.initialization;

import com.s26462.questionnaire.users.Role;
import com.s26462.questionnaire.users.User;
import com.s26462.questionnaire.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setRole(Role.ADMIN);
        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            userRepository.save(user);
        }
    }
}
