package org.adelok.common.handler;

import io.jsonwebtoken.JwtException;
import org.adelok.common.error.ErrorCode;
import org.adelok.common.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.adelok.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handle(InsufficientFundsException exception, HttpServletRequest request) {
        log.warn("INSUFFICIENT FUNDS");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.INSUFFICIENT_FUNDS,
                HttpStatus.UNPROCESSABLE_CONTENT,
                exception.getMessage(),
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(errorResponse);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(ResourceNotFoundException exception, HttpServletRequest request) {
        log.warn("RESOURCE NOT FOUND");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.NOT_FOUND,
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(WalletAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handle(WalletAlreadyExistsException exception, HttpServletRequest request) {
        log.warn("WALLET ALREADY EXISTS");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.WALLET_ALREADY_EXISTS,
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    @ExceptionHandler(WalletNotActiveException.class)
    public ResponseEntity<ErrorResponse> handle(WalletNotActiveException exception, HttpServletRequest request) {
        log.warn("WALLET NOT ACTIVE");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.WALLET_NOT_ACTIVE,
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.warn("ARGUMENT NOT VALID");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.VALIDATION_FAILED,
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception exception, HttpServletRequest request) {
        log.error("UNEXPECTED ERROR AT {}", request.getRequestURI(), exception);
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.INTERNAL_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "UNEXPECTED ERROR",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handle(IllegalArgumentException exception, HttpServletRequest request) {
        log.warn("STEAMID CANT BE NULL");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.BAD_REQUEST,
                HttpStatus.BAD_REQUEST,
                "PLEASE, ENTER STEAM ID",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(UsernameNotFoundException exception, HttpServletRequest request) {
        log.warn("THERES NO USER WITH THIS USERNAME");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED,
                "USER NOT FOUND WITH THIS USERNAME",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handle(JwtException exception, HttpServletRequest request) {
        log.warn("INVALID TOKEN");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED,
                "INVALID TOKEN, MAYBE YOUR TOKEN IS EXPIRED",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handle(InvalidTokenException exception, HttpServletRequest request) {
        log.warn("INVALID REFRESH TOKEN {} {}", request.getRequestURI(), exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED,
                "INVALID REFRESH TOKEN",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handle(ResponseStatusException exception, HttpServletRequest request) {
        log.warn("Http error: {}", exception.getReason());
        ErrorCode code = switch (exception.getStatusCode().value()) {
            case 404 -> ErrorCode.NOT_FOUND;
            case 409 -> ErrorCode.BAD_REQUEST;
            case 403 -> ErrorCode.FORBIDDEN;
            default -> ErrorCode.INTERNAL_ERROR;
        };
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                code,
                HttpStatus.valueOf(exception.getStatusCode().value()),
                exception.getReason(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(exception.getStatusCode()).body(error);
    }
}
