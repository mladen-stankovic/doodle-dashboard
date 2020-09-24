package com.doodle.doodledashboard.controller;

import com.doodle.doodledashboard.payload.response.ErrorResponse;
import org.springframework.http.ResponseEntity;

/**
 * Created by mladen.stankovic on 2020-08-21.
 */
public abstract class BaseController {
    protected ResponseEntity badRequest(ErrorResponse errorResponse) {
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
}
