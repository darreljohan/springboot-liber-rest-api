package com.iglo.exam.liber.error;

import com.iglo.exam.liber.error.exception.DeletionConflict;
import com.iglo.exam.liber.error.exception.DuplicateNameException;
import com.iglo.exam.liber.error.exception.ResourceNotFound;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorExceptionHandler {
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorMessageResponse<Object>> handleResourceNotFound(ResourceNotFound e){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorMessageResponse<Object> errorMessageResponse = ErrorMessageResponse.builder()
                .status(httpStatus)
                .message("Resource Not Found" )
                .errors(e.getMessage())
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageResponse);
    }

    @ExceptionHandler(DeletionConflict.class)
    public ResponseEntity<ErrorMessageResponse<Object>> handleDeletionConflict(DeletionConflict e){
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ErrorMessageResponse<Object> errorMessageResponse = ErrorMessageResponse.builder()
                .status(httpStatus)
                .message("Deletion Conflict" )
                .errors(e.getMessage())
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageResponse);
    }

    @ExceptionHandler(DuplicateNameException.class)
    public ResponseEntity<ErrorMessageResponse<Object>> handleDuplicateNameException(DuplicateNameException e){
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ErrorMessageResponse<Object> errorMessageResponse = ErrorMessageResponse.builder()
                .status(httpStatus)
                .message("Duplicate Name Conflict" )
                .errors(e.getMessage())
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageResponse);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorMessageResponse<Object>> handlePropertyReferenceException(PropertyReferenceException e){
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ErrorMessageResponse<Object> errorMessageResponse = ErrorMessageResponse.builder()
                .status(httpStatus)
                .message("Property Reference Error" )
                .errors(e.getMessage())
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageResponse);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorMessageResponse<Object>> handleNumberFormatException(NumberFormatException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorMessageResponse<Object> errorMessageResponse = ErrorMessageResponse.builder()
                .status(httpStatus)
                .message("Number Format Error" )
                .errors("Invalid number format: " + e.getMessage())
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageResponse);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorMessageResponse<Object>> handleDateTimeParseException(NumberFormatException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorMessageResponse<Object> errorMessageResponse = ErrorMessageResponse.builder()
                .status(httpStatus)
                .message("Date Format Error" )
                .errors("Invalid Date format: " + e.getMessage())
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorMessageResponse<Object> errorMessageResponse = ErrorMessageResponse.builder()
                .status(httpStatus)
                .message("JSON Malformed: " + e.getCause().getMessage())
                .errors(e.getMostSpecificCause().getLocalizedMessage())
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessageResponse<Object>> handleAuthenticationException(AuthenticationException e){
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ErrorMessageResponse<Object> errorMessageResponse = ErrorMessageResponse.builder()
                .status(httpStatus)
                .message("Authentication Failed")
                .errors("Invalid username or password")
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Map<String, String> errorsMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        //<Map<String, String>>builder() 
        ErrorMessageResponse<Map<String, String>> errorMessageResponse = ErrorMessageResponse.<Map<String, String>>builder().status(httpStatus)
                .message(e.getMessage())
                .errors(errorsMap)
                .build();

        return ResponseEntity.status(httpStatus).body(errorMessageResponse);
    }
}
