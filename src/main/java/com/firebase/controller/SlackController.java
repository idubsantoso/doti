package com.firebase.controller;

import com.firebase.dto.SlackRequest;
import com.firebase.service.SlackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/slacks")
public class SlackController {
    @Autowired
    private SlackService service;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping()
    public ResponseEntity<Object> externalSlackAPI() throws ExecutionException, InterruptedException, ParseException {
        return ResponseEntity.ok(service.sendToSlack());
    }

}
