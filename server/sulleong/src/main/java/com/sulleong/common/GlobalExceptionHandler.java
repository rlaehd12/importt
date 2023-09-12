package com.sulleong.common;

import com.sulleong.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorMessage buildErrorMessage(Exception exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return ErrorMessage.builder()
                .title(exception.getClass().getSimpleName())
                .detail(exception.getMessage())
                .instance(request.getRequestURI())
                .build();
    }

    @ExceptionHandler({
            AgeRangeException.class
    })
    public ResponseEntity<ErrorMessage> handleBadRequest(Exception e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorMessage(e, request),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            NotLoginException.class,
            AccessTokenExpiredException.class,
            GoogleOauthLoginException.class
    })
    public ResponseEntity<Void> handleAuthFail() {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(URI.create("/login")).build();
    }

    @ExceptionHandler({
            BeerNotFoundException.class,
            MemberNotFoundException.class
    })
    public ResponseEntity<ErrorMessage> handleNotFound(Exception e, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorMessage(e, request),
                HttpStatus.FORBIDDEN);
    }
}
