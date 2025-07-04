package com.lasha.personal_api_rate_limiter_service.controller;

import com.lasha.personal_api_rate_limiter_service.dto.ClientRegisterRequest;
import com.lasha.personal_api_rate_limiter_service.dto.ClientRegisterResponse;
import com.lasha.personal_api_rate_limiter_service.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<ClientRegisterResponse> registerClient(@RequestBody ClientRegisterRequest clientRegisterRequest) {
        return new ResponseEntity<ClientRegisterResponse>(clientService.registerClient(clientRegisterRequest), HttpStatus.OK);
    }
}
