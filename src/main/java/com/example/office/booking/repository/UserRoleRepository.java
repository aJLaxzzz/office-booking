package com.example.office.booking.repository;

import com.example.office.booking.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    UserRole getUserRoleById(Long userId);
}
