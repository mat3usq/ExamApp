package com.example.exams.Model.Data.ProperDataModels;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class User {
    @NotBlank(message = "First name is mandatory")
    @Size(max = 20, message = "First name cannot be longer than 20 characters")
    @Pattern(regexp = "^[A-Za-z-\\s]+$", message = "First name can only contain letters, spaces, and hyphens")
    private String firstname;

    @Size(max = 20, message = "Last name cannot be longer than 20 characters")
    @Pattern(regexp = "^[A-Za-z-\\s]*$", message = "Last name can only contain letters, spaces, and hyphens")
    private String lastname;

    @NotBlank(message = "Login is mandatory")
    @Size(max = 20, message = "Login cannot be longer than 20 characters")
    private String login;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 1, max = 20, message = "Password must be between 8 and 20 characters long")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 40, message = "Email cannot be longer than 40 characters")
    private String email;
}