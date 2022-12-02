package ru.practicum.explore.with.me.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends RuntimeException {

    public ValidationException(String message, String param) {
        super(message);
        log.info("Validation error: {}. Sent value {}", message, param);
    }

}
