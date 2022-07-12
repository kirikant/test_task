package com.antipov.task.crypto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CryptoCurrencyNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(CryptoCurrencyNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e) {
        List<ValidationError> validationExceptions = e.getValidationExceptions();
        HashMap<String, Object> map = new HashMap<>();
        for (ValidationError exception :
                validationExceptions) {
            map.put("incorrect field: ",exception.getField());
            map.put("message:", exception.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleServerExceptions(Throwable e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("server was unable to process the request correctly");
    }


}
