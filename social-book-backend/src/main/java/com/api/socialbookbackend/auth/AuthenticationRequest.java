package com.api.socialbookbackend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(@NotEmpty(message = "email is required")
                                    @NotBlank(message = "email is required")
                                    @Email(message = "email must be a valid email address")
                                    String email,

                                    @NotEmpty(message = "password is required")
                                    @NotBlank (message = "password is required")
                                    @Size(min = 8, message = "password must be at least 8 characters long")
                                    String password) {
}
