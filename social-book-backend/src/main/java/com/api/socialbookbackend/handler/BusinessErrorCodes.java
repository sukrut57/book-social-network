package com.api.socialbookbackend.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * Business error codes
 */
public enum BusinessErrorCodes {
    NO_CODE(0, "No code", NOT_IMPLEMENTED),

    INCORRECT_CURRENT_PASSWORD(300, "Incorrect current password", BAD_REQUEST),

    INCORRECT_NEW_PASSWORD(301, "The new password does not match", BAD_REQUEST),

    ACCOUNT_LOCKED(302, "Account locked", FORBIDDEN),

    ACCOUNT_DISABLED(303, "Account is disabled", FORBIDDEN),

    BAD_CREDENTIALS(304, "Login and / or password is incorrect", FORBIDDEN),
    ;

    @Getter
    private final int code;

    @Getter
    private final String description;

    @Getter
    private final HttpStatus httpStatus;

     BusinessErrorCodes(int code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
