package com.smp.notification.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String userMessage;
    private String technicalMessage;
    private LocalDateTime timestamp;
}