package com.lasha.personal_api_rate_limiter_service.controller;

import com.lasha.personal_api_rate_limiter_service.dto.RateLimitInfoResponse;
import com.lasha.personal_api_rate_limiter_service.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rate-limit")
public class RateLimitController {
    private final RateLimitService rateLimitService;

    public RateLimitController(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @PostMapping("/check")
    public ResponseEntity<RateLimitInfoResponse> checkUserRateLimit(HttpServletRequest request) {
        return rateLimitService.checkUserLimit(request);
    }
}
