package com.mergevisualizer.controller;

import com.mergevisualizer.dto.ErrorResponse;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException exception) {
        return buildError(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException exception) {
        String message = exception.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (isBadRepositoryRequest(message)) {
            status = HttpStatus.BAD_REQUEST;
        }

        return buildError(status, getFriendlyMessage(message));
    }

    @ExceptionHandler(GitAPIException.class)
    public ResponseEntity<ErrorResponse> handleGitApiException() {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Could not read repository data. Please try another repository.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException() {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Please try again.");
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), message));
    }

    private boolean isBadRepositoryRequest(String message) {
        if (message == null) {
            return false;
        }

        String lowerMessage = message.toLowerCase();
        return lowerMessage.contains("invalid zip")
                || lowerMessage.contains("no git repository")
                || lowerMessage.contains("outside of the target dir");
    }

    private String getFriendlyMessage(String message) {
        if (message == null || message.isBlank()) {
            return "Could not process the repository. Please try again.";
        }

        return message;
    }
}
