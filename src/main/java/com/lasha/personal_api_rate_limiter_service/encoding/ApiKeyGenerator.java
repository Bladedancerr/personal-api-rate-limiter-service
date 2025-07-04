package com.lasha.personal_api_rate_limiter_service.encoding;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class ApiKeyGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    public String generateApiKey() {
        String unique = UUID.randomUUID().toString();

        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String randomString = base64Encoder.encodeToString(bytes);
        return unique + "." + randomString;
    }

}
