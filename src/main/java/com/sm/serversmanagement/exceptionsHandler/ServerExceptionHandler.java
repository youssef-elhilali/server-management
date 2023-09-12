package com.sm.serversmanagement.exceptionsHandler;

import com.sm.serversmanagement.apiResponse.ApiResponseFailed;
import com.sm.serversmanagement.exceptions.PaginationException;
import com.sm.serversmanagement.exceptions.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ServerExceptionHandler {

    @ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponseFailed> handleDocumentNotFoundException(ServerException exception){
        return ResponseEntity.ok(
                ApiResponseFailed.builder()
                        .time(LocalDateTime.now())
                        .message(exception.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build()
        );
    }

    @ExceptionHandler(PaginationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseFailed> handlePaginationException(PaginationException exception){
        return ResponseEntity.ok(
                ApiResponseFailed.builder()
                        .time(LocalDateTime.now())
                        .message(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseFailed> handleMethodArgumentException(MethodArgumentNotValidException exception) {

        List<String> errorTable = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(
                fieldError -> {
                    errorTable.add(fieldError.getField());
                }
        );
        exception.getBindingResult().getFieldErrors().forEach(
                fieldError -> {
                    errorTable.add(fieldError.getDefaultMessage());
                }
        );
        return ResponseEntity.ok(
                ApiResponseFailed.builder()
                        .field(errorTable.get(0))
                        .time(LocalDateTime.now())
                        .message(errorTable.get(1))
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

}
