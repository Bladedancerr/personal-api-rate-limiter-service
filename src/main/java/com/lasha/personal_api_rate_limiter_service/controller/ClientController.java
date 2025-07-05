package com.lasha.personal_api_rate_limiter_service.controller;

import com.lasha.personal_api_rate_limiter_service.dto.ClientRegisterRequest;
import com.lasha.personal_api_rate_limiter_service.dto.ClientRegisterResponse;
import com.lasha.personal_api_rate_limiter_service.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<ClientRegisterResponse> registerClient(@RequestBody ClientRegisterRequest clientRegisterRequest) {
        return new ResponseEntity<ClientRegisterResponse>(clientService.registerClient(clientRegisterRequest), HttpStatus.OK);
    }

    @PostMapping("/login")
    public String test() {
        System.out.println("Login endpoint hit");
        return "test";
    }
}
