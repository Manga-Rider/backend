package com.mangarider.exception.handler;

import com.mangarider.model.dto.response.ErrorMessageResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@RequiredArgsConstructor
public class ValidationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        HttpStatus status = BAD_REQUEST;
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        ErrorMessageResponse response = getErrorMessageResponse(e, status);
        List<?> additionalMessages = violations.stream()
                .map(exception -> ErrorMessage.builder()
                        .message(exception.getMessageTemplate())
                        .invalidValue(exception.getInvalidValue())
                        .build()
                ).toList();
        response.details().put("errors", additionalMessages);

        return ResponseEntity.status(status).body(response);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HttpStatus status = BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(status).body(errors);
    }

    private ErrorMessageResponse getErrorMessageResponse(Throwable e, HttpStatus status) {
        return ErrorMessageResponse.builder()
                .createdAt(Instant.now())
                .statusCode(status)
                .status(status.value())
                .message(e.getMessage())
                .details(new HashMap<>())
                .build();
    }



    @Builder
    private record ErrorMessage(
            String message,
            Object invalidValue

    ) {
    }
}