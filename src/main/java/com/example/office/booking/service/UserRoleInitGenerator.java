package com.example.office.booking.service;

import com.example.office.booking.entity.UserRole;
import com.example.office.booking.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleInitGenerator {
    @Autowired
    private UserRoleRepository userRoleRepository;

    private static final List<UserRole> usersRoles = List.of(
        new UserRole(UserRole.Role.ADMIN, 1L),
        new UserRole(UserRole.Role.USER, 2L)
    );

    @PostConstruct
    public void generateUsersRoles() {
        userRoleRepository.saveAll(usersRoles);
    }
}
