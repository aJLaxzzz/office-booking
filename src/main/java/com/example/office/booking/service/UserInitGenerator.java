package com.example.office.booking.service;

import com.example.office.booking.entity.User;
import com.example.office.booking.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInitGenerator {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void generateUsers() {
        List<User> users = List.of(
                new User(
                        null,
                        "admin",
                        "admin@gmail.com",
                        passwordEncoder.encode("admin")
                ),
                new User(
                        null,
                        "user",
                        "user@yandex.ru",
                        passwordEncoder.encode("user")
                )
        );
        userRepository.saveAll(users);
    }
}
