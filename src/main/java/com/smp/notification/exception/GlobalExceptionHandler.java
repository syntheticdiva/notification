package com.smp.notification.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProcessingException.class)
    public ResponseEntity<ErrorResponse> handleProcessingError(ProcessingException e) {
        logError("Ошибка обработки уведомления", e);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ошибка обработки", e.getMessage());
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotificationNotFound(NotificationNotFoundException e) {
        logError("Уведомление не найдено", e);
        return createErrorResponse(HttpStatus.NOT_FOUND,
                "Уведомление не найдено", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception e) {
        logError("Системная ошибка", e);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Произошла системная ошибка",
                "Пожалуйста, попробуйте позже или обратитесь к администратору");
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status,
                                                              String userMessage,
                                                              String technicalMessage) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                userMessage,
                technicalMessage,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    private void logError(String message, Exception e) {
        MDC.put("exceptionClass", e.getClass().getName());
        MDC.put("exceptionMessage", e.getMessage());
        log.error(message, e);
        MDC.clear();
    }
}