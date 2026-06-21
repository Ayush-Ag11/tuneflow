package com.tuneflow.music_service.exception;

import com.tuneflow.music_service.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error ->
                        error.getField() + ": "
                                + error.getDefaultMessage())
                .orElse("Validation failed");

        ErrorResponse response =
                new ErrorResponse(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "VALIDATION_ERROR",
                        message
                );

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {

        ErrorResponse response =
                new ErrorResponse(
                        Instant.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "RESOURCE_NOT_FOUND",
                        ex.getMessage()
                );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(
            DuplicateResourceException ex) {

        ErrorResponse response =
                new ErrorResponse(
                        Instant.now(),
                        HttpStatus.CONFLICT.value(),
                        "DUPLICATE_RESOURCE",
                        ex.getMessage()
                );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }
}