package com.api.socialbookbackend.handler;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.api.socialbookbackend.handler.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles the Locked exception ; Spring security exception
     * @param lockedException the exception to handle
     * @return the response entity
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException lockedException){
        return ResponseEntity.status(UNAUTHORIZED).body(ExceptionResponse.builder()
                .businessErrorCode(ACCOUNT_LOCKED.getCode())
                .businessErrorDescription(ACCOUNT_LOCKED.getDescription())
                .error(lockedException.getMessage())
                .build());
    }

    /**
     * Handles the Disabled exception ; Spring security exception
     * @param disabledException the exception to handle
     * @return the response entity
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException disabledException){
        return ResponseEntity.status(UNAUTHORIZED).body(ExceptionResponse.builder()
                .businessErrorCode(ACCOUNT_DISABLED.getCode())
                .businessErrorDescription(ACCOUNT_DISABLED.getDescription())
                .error(disabledException.getMessage())
                .build());
    }

    /**
     * Handles the Bad credentials exception ; Spring security exception
     * @param badCredentialsException the exception to handle
     * @return the response entity
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException badCredentialsException){
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                .businessErrorCode(BAD_CREDENTIALS.getCode())
                .businessErrorDescription(BAD_CREDENTIALS.getDescription())
                .error(badCredentialsException.getMessage())
                .build());
    }

    /**
     * Handles the Messaging exception ; Spring mail exception
     * @param messagingException the exception to handle
     * @return the response entity
     */
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException messagingException){
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                .error(messagingException.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException methodArgumentNotValidException){
        Set<String> errors = new HashSet<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errors.add(fieldError.getDefaultMessage()));

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ExceptionResponse.builder().validationErrors(errors)
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception){
        log.error("An unexpected error occurred", exception);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Internal Server Error")
                        .error(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException exception){
        log.error("An unexpected error occurred", exception);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Bad Request")
                        .error(exception.getMessage())
                        .build());
    }

}
