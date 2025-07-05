package com.lasha.personal_api_rate_limiter_service.exceptions;

import com.lasha.personal_api_rate_limiter_service.pojo.RateLimitInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClientExceptionsHandler {

    @ExceptionHandler(ApiKeyGenerationException.class)
    public ResponseEntity<ErrorResponse> handleApiKeyGenerationException(ApiKeyGenerationException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEmailFormatException(InvalidEmailFormatException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY); // 422
    }

    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<ErrorResponse> handleUserDisabledException(UserDisabledException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidApiKeyException.class)
    public ResponseEntity<ErrorResponse> handleInvalidApiKeyException(InvalidApiKeyException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserIdException(InvalidUserIdException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientDisabledException.class)
    public ResponseEntity<ErrorResponse> handleClientDisabledException(ClientDisabledException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceededException(RateLimitExceededException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(UserLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleUserLimitExceededException(UserLimitExceededException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS.value(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.TOO_MANY_REQUESTS);
    }
}