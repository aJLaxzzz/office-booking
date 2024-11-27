package com.example.office.booking.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String confirmPassword;

    public boolean validPasswords() {
        return password.equals(confirmPassword);
    }
}