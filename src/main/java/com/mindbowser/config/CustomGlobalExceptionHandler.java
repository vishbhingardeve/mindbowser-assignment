package com.mindbowser.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import com.mindbowser.dto.ApiMsgResponse;
import com.mindbowser.entity.ErrorCode;
import com.mindbowser.exception.BadRequestException;
import com.mindbowser.exception.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // @Validate For Validating Path Variables and Request Parameters
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex) throws IOException {

        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        return buildResponseEntity(new ApiMsgResponse(ErrorCode.BAD_REQUEST.getCode(), ex.getMessage(), errors, null), HttpStatus.BAD_REQUEST);
    }

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        // Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);
        return buildResponseEntity(new ApiMsgResponse(HttpStatus.BAD_REQUEST.value(), "fail", errors), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        return buildResponseEntity(new ApiMsgResponse(HttpStatus.BAD_REQUEST.value(), "fail", Collections.singletonList("invalid JSON request body")),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Object> handleHttpServerErrorException(HttpServerErrorException ex) throws IOException {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        return buildResponseEntity(new ApiMsgResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), "fail", errors), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException ex) throws IOException {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        return buildResponseEntity(new ApiMsgResponse(HttpStatus.NOT_FOUND.value(), "fail", errors), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Object> handleResourceAccessException(ResourceAccessException ex) throws IOException {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        return buildResponseEntity(new ApiMsgResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), "fail", errors), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) throws IOException {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        return buildResponseEntity(new ApiMsgResponse(ex.getStatusCode()
                .value(), "fail", errors), ex.getStatusCode());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) throws IOException {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        return buildResponseEntity(new ApiMsgResponse(HttpStatus.NOT_FOUND.value(), "fail", errors), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(BadCredentialsException ex) throws IOException {
        List<String> errors = new ArrayList<>();
        log.error("Error occurred ", ex.getLocalizedMessage());
        errors.add(ex.getLocalizedMessage());
        ex.printStackTrace();
            if (ex.getLocalizedMessage()
                    .equals("Bad credentials")) {
                final ApiMsgResponse apiError = new ApiMsgResponse(HttpStatus.FORBIDDEN.value(), "fail",
                        Collections.singletonList("Please enter correct password."));
                return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
            }
            return null;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) throws IOException {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        log.info(ex.getClass()
                .getName(), ex.getLocalizedMessage());
        return buildResponseEntity(new ApiMsgResponse(HttpStatus.BAD_REQUEST.value(), "fail", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataException(DataIntegrityViolationException ex) throws IOException {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        log.info(ex.getClass()
                .getName(), ex.getLocalizedMessage());
        return buildResponseEntity(new ApiMsgResponse(HttpStatus.BAD_REQUEST.value(), "fail", "Invalid date or format!! It should be YYYY-MM-DD."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {

        log.error("Error occurred ", ex.getLocalizedMessage());
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        log.error("Opps! Error log", ex);
        try {
            if (ex.getLocalizedMessage()
                    .equals("Access is denied")) {
                final ApiMsgResponse apiError = new ApiMsgResponse(HttpStatus.FORBIDDEN.value(), "fail",
                        Collections.singletonList("Please sign in first to access this resource."));
                return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        } finally {
            log.info("Enter: exception Exception Type: Exception: Request Arguments: {}");
        }

        return buildResponseEntity(new ApiMsgResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), errors, "some thing went wrong"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getReason());
        return buildResponseEntity(new ApiMsgResponse(ex.getStatus()
                .value(), "fail", errors), ex.getStatus());
    }

    private ResponseEntity<Object> buildResponseEntity(ApiMsgResponse apiError, HttpStatus status) {
        return new ResponseEntity<>(apiError, status);
    }
}
