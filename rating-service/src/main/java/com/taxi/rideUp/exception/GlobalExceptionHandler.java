package com.taxi.rideUp.exception;

import com.taxi.rideUp.dto.response.ErrorResponse;
import com.taxi.rideUp.exception.external.DriveManageValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * packageName : com.taxi.rideUp.exception
 * fileName    : GlobalExceptionHandler
 * author      : ckr
 * date        : 25. 8. 2.
 * description : 전역 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(DriveManageValidationException.class)
    public ResponseEntity<ErrorResponse> handleDriveManageValidationException(DriveManageValidationException ex) {
        log.error("DriveManage validation failed", ex);

        ErrorResponse errorResponse = new ErrorResponse("DriveManage validation failed", ex.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation failed", ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred", ex);

        ErrorResponse errorResponse = new ErrorResponse("Internal server error", ex.getMessage());

        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
