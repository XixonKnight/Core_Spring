package com.example.corespring.exceptions;


import com.example.corespring.utils.CommonUtils;
import com.example.corespring.core.constants.Constants;
import com.example.corespring.core.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<Object> handlingException(Exception exception, WebRequest request) {
        log.error("Exception on Request: {}", request.getDescription(false), exception);
        Response response = Response.error(Constants.ResponseCode.BAD_REQUEST);
        response.setMessage(exception.getMessage());
        response.setRequestId(CommonUtils.getRequestId());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = ApplicationException.class)
    ResponseEntity<Object> handlingApplicationException(ApplicationException exception, WebRequest request) {
        log.error("Exception on Request: {}", request.getDescription(false), exception);
        Response response = Response.error(exception.getErrorCode().name(), String.valueOf(exception.getErrorCode().getCode()));
        response.setMessage(exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage());
        response.setRequestId(CommonUtils.getRequestId());
        return ResponseEntity.status(exception.getErrorCode().getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<Object> handlingValidation(MethodArgumentNotValidException exception, WebRequest request) {
        log.error("Exception on Request: {}", request.getDescription(false), exception);
        BindingResult bindingResult = exception.getBindingResult();
        Map<String, Object> fieldErrors = bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                error -> {
                                    assert error.getCodes() != null;
                                    return Optional.ofNullable(error.getCodes()[3]).orElse("Invalid");
                                },
                                Collectors.collectingAndThen(Collectors.toList(), list -> list)
                        )
                ));
        Response response = Response.error(Constants.ResponseCode.BAD_REQUEST);
        response.setRequestId(CommonUtils.getRequestId());
        response.setFieldError(fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, WebRequest request) {
        log.error("Exception on Request: {}", request.getDescription(false), exception);
        Response response = Response.error(Constants.ResponseCode.BAD_REQUEST);
        response.setRequestId(CommonUtils.getRequestId());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
