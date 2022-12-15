package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return Map.of("error", e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElseGet(() -> "one of the fields is entered incorrectly"));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error(e.getMessage());
        return Map.of("error", e.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElseGet(() -> "one of the fields is entered incorrectly"));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> constraintViolationExceptionHandler(MissingRequestHeaderException e) {
        log.error(e.getMessage());
        return Map.of("error", String.format("request header: %s is entered incorrectly", e.getHeaderName()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFoundExceptionHandler(NotFoundException e) {
        log.error(e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowable(final Throwable e) {
        log.error(e.getMessage());
        return Map.of("error", e.getMessage());
    }
}
