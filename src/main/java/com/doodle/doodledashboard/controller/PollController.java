package com.doodle.doodledashboard.controller;

import com.doodle.doodledashboard.common.UriMappingConstants;
import com.doodle.doodledashboard.payload.response.ErrorResponse;
import com.doodle.doodledashboard.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * Created by mladen.stankovic on 2020-09-24.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@RequestMapping(UriMappingConstants.POLLS)
public class PollController extends BaseController {
    private final PollService pollService;

    @Autowired
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping(UriMappingConstants.INITIATOR + "/{initiatorEmail}")
    public ResponseEntity<?> findByInitiatorEmail(@PathVariable("initiatorEmail") @NotEmpty @Email String initiatorEmail) {
        return ResponseEntity.ok(pollService.findByInitiatorEmail(initiatorEmail));
    }

    @GetMapping(UriMappingConstants.TITLE + "/{title}")
    public ResponseEntity<?> searchByTitle(@PathVariable("title") @NotEmpty String title) {
        return ResponseEntity.ok(pollService.searchByTitle(title));
    }

    @GetMapping(UriMappingConstants.CREATED_AFTER + "/{dateString}")
    public ResponseEntity<?> findCreatedAfterDate(@PathVariable("dateString") @NotEmpty String dateString, HttpServletRequest request) {
        String errorMessage = null;
        Long unixTimestamp = null;
        try {
            unixTimestamp = Long.parseLong(dateString);
        } catch (NumberFormatException e) {
            errorMessage = "Please provide valid date.";
        }

        if (unixTimestamp != null) {
            Date providedDate = new Date(unixTimestamp);
            if (providedDate.after(new Date())) {
                errorMessage = "Please provide date in the past.";
            }
        }

        if (errorMessage != null) {
            ErrorResponse errorResponse = new ErrorResponse(request, HttpStatus.BAD_REQUEST.value(),
                    "Constraint violation error", errorMessage);
            return badRequest(errorResponse);
        }

        return ResponseEntity.ok(pollService.findCreatedAfterDate(unixTimestamp));
    }
}
