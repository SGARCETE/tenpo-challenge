package com.tenpo.challenge.config;

import com.tenpo.challenge.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  Handle all controllers errors and returns them with a specific message for the users.
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(value = {TokenNotValidException.class})
    public ResponseEntity<ApiError> tokenNotValidException(TokenNotValidException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("Token not valid exception", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {UserNotLoggedException.class})
    public ResponseEntity<ApiError> userNotLoggedException(UserNotLoggedException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("User not logged exception", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {UserAlreadyLoggedException.class})
    public ResponseEntity<ApiError> userAlreadyLoggedException(UserAlreadyLoggedException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("User already logged exception", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {PasswordNotValidException.class})
    public ResponseEntity<ApiError> passwordNotValidException(PasswordNotValidException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("Password not valid exception", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ApiError> userNotFoundException(UserNotFoundException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("User not found Exception", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<ApiError> userAlreadyExistsException(UserAlreadyExistsException ex) {
        LOGGER.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("User already exists Exception", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOGGER.warn(String.format("Exception %s was thrown", ex.getClass()));

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Method Argument Not Valid");

        //Get all errors
        List<String> errors = ex.getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        LOGGER.warn(String.format("Errors: %s", errors));

        body.put("errors", errors);
        return ResponseEntity.badRequest().body(body);
    }
}
