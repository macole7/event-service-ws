package com.service.eventservice.exception;

import java.time.LocalDate;

public class ExceptionResponse {
    private LocalDate timestamp;
    private String message;
    private String details;

    ExceptionResponse(LocalDate timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
