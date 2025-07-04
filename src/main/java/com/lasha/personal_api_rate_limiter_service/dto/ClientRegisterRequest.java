package com.lasha.personal_api_rate_limiter_service.dto;

public class ClientRegisterRequest {
    private String email;
    private String password;

    public ClientRegisterRequest() {
    }

    public ClientRegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
