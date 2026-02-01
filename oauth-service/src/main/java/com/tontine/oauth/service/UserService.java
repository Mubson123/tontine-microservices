package com.tontine.oauth.service;

import com.tontine.customer.proto.CustomerEvent;
import com.tontine.oauth.models.Role;
import com.tontine.oauth.models.User;
import com.tontine.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUserFromCustomer(CustomerEvent event) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(event.getEmail()))) {
            return;
        }
        String randomPassword = SecureRandomPasswordGenerator.generate();
        User user = new User();
        user.setFirstname(event.getFirstname());
        user.setLastname(event.getLastname());
        user.setEmail(event.getEmail());
        user.setPassword(passwordEncoder.encode(randomPassword));
        user.setRole(Role.USER);
        user.setEnabled(true);
        user.setMustChangePassword(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private static final class SecureRandomPasswordGenerator {

        private static final String CHARS =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";

        public static String generate() {
            SecureRandom random = new SecureRandom();
            return random.ints(12, 0, CHARS.length())
                    .mapToObj(CHARS::charAt)
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        }
    }

}
