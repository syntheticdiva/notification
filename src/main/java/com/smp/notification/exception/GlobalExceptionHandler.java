package com.smp.notification.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleProcessingError(ProcessingException e, Model model) {
        logError("Ошибка обработки", e);
        return createErrorResponse("Произошла ошибка обработки запроса", e.getMessage(), model);
    }

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidInput(InvalidInputException e, Model model) {
        logError("Неверный ввод", e);
        return createErrorResponse("Неверный ввод данных", e.getMessage(), model);
    }

    @ExceptionHandler(ExternalServiceException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleExternalServiceError(ExternalServiceException e, Model model) {
        logError("Ошибка внешнего сервиса", e);
        return createErrorResponse("Сервис временно недоступен", "Пожалуйста, попробуйте позже", model);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericError(Exception e, Model model) {
        logError("Произошла системная ошибка", e);
        return createErrorResponse("Произошла системная ошибка", "Пожалуйста, попробуйте позже или обратитесь к администратору", model);
    }

    protected String createErrorResponse(String userMessage, String technicalMessage, Model model) {
        model.addAttribute("errorMessage", userMessage);
        model.addAttribute("technicalDetails", technicalMessage);
        model.addAttribute("timestamp", new Date());
        return "error";
    }

    private void logError(String message, Exception e) {
        MDC.put("exceptionClass", e.getClass().getName());
        MDC.put("exceptionMessage", e.getMessage());
        log.error(message, e);
        MDC.clear();
    }
}