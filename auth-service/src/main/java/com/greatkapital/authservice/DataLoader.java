package com.greatkapital.authservice;

import com.greatkapital.authservice.entity.User;
import com.greatkapital.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("writer").isEmpty()) {
            User writer = new User();
            writer.setUsername("writer");
            writer.setPasswordHash(passwordEncoder.encode("password"));
            writer.setRoles(List.of("ROLE_message_writer"));
            userRepository.save(writer);
        }
        if (userRepository.findByUsername("reader").isEmpty()) {
            User reader = new User();
            reader.setUsername("reader");
            reader.setPasswordHash(passwordEncoder.encode("password"));
            reader.setRoles(List.of("ROLE_message_reader"));
            userRepository.save(reader);
        }
    }
}