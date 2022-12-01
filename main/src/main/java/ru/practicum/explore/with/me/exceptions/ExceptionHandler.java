package ru.practicum.explore.with.me.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<?> validationException(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<?> notFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ResponseStatus(
            value = HttpStatus.CONFLICT,
            reason = "дубликаты в данных")
    @org.springframework.web.bind.annotation.ExceptionHandler(ConflictException.class)
    public void handleException(ConflictException e) {
    }

}
