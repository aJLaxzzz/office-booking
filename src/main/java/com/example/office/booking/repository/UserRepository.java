package com.example.office.booking.repository;

import com.example.office.booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByName(String username);
    User getUserByName(String username);
    boolean existsByName(String username);
    Optional<User> findById(Long id);
}
