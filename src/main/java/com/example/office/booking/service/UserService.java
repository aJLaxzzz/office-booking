package com.example.office.booking.service;

import com.example.office.booking.entity.User;
import com.example.office.booking.entity.UserDTO;
import com.example.office.booking.entity.UserRole;
import com.example.office.booking.repository.UserRepository;
import com.example.office.booking.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder encoder;

    public User getUserByName(String userName) {
        return userRepository.getUserByName(userName);
    }

    public Optional<User> findUserByName(String username) {
        return userRepository.findUserByName(username);
    }

    public boolean addUser(UserDTO newUser) {
        if (!userRepository.existsByName(newUser.getName())) {
            User user = new User(
                    null,
                    newUser.getName(),
                    newUser.getEmail(),
                    encoder.encode(newUser.getPassword())
            );
            userRepository.save(user);

            User savedUser = userRepository.getUserByName(user.getName());
            UserRole userRole = new UserRole(
                    UserRole.Role.USER,
                    savedUser.getId()
            );
            userRoleRepository.save(userRole);

            return true;
        }
        return false;
    }
}
