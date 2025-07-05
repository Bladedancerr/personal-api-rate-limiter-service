package com.lasha.personal_api_rate_limiter_service.controller;

import com.lasha.personal_api_rate_limiter_service.dto.RateLimitInfoResponse;
import com.lasha.personal_api_rate_limiter_service.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rate-limit")
public class RateLimitController {
    private final RateLimitService rateLimitService;

    @Autowired
    public RateLimitController(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @PostMapping("/check")
    public ResponseEntity<RateLimitInfoResponse> checkUserRateLimit(HttpServletRequest request) {
        return new ResponseEntity<>(rateLimitService.checkUserLimit(request), HttpStatus.OK);
    }
}
