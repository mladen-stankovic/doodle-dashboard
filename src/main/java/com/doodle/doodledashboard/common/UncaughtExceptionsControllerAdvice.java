package com.doodle.doodledashboard.common;

import com.doodle.doodledashboard.payload.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Date;

/**
 * Created by mladen.stankovic on 2020-09-24.
 */
@ControllerAdvice(annotations = {RestController.class})
public class UncaughtExceptionsControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity handleBindingErrors(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(request, HttpStatus.BAD_REQUEST.value(),
                "Constraint violation error", ex.getLocalizedMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
