package com.lasha.personal_api_rate_limiter_service.encoding;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Sha256Hasher {

    private final MessageDigest messageDigest;

    public Sha256Hasher() throws NoSuchAlgorithmException {
        this.messageDigest = MessageDigest.getInstance("SHA-256");
    }

    // Returns the hashed value as a byte array
    public byte[] hashToBytes(String input) {
        return messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    // Returns the hashed value as a Base64 string
    public String hashToBase64(String input) {
        byte[] hashedBytes = hashToBytes(input);
        return java.util.Base64.getEncoder().encodeToString(hashedBytes);
    }

    // Returns the hashed value as a Hex string
    public String hashToHex(String input) {
        byte[] hashedBytes = hashToBytes(input);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}