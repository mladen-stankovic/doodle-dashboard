package com.doodle.doodledashboard.controller;

import com.doodle.doodledashboard.common.UriMappingConstants;
import com.doodle.doodledashboard.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
}
