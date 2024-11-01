package com.api.socialbookbackend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record RegistrationRequest(

        @NotEmpty(message = "email is required")
        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid email address")
        String email,

        @NotEmpty(message = "password is required")
        @NotBlank (message = "password is required")
        @Size(min = 8, message = "password must be at least 8 characters long")
        String password,

        @NotEmpty(message = "firstName is required")
        @NotBlank(message = "firstName is required")
        String firstName,

        @NotEmpty (message = "lastName is required")
        @NotBlank(message = "lastName is required")
        String lastName,

        @NotEmpty(message = "dateOfBirth is required")
        @NotBlank(message = "dateOfBirth is required")
        String dateOfBirth
) {
}
