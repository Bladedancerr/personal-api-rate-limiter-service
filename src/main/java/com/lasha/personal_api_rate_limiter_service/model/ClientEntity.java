package com.lasha.personal_api_rate_limiter_service.model;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "client")
public class ClientEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "api_key")
    private  String apiKey;
    @Column(name = "rate_limit")
    private int rateLimit;
    @Column(name = "user_limit")
    private int userLimit;
    @Column(name = "rate_window_sec")
    private long rateWindowSeconds;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "created_at")
    private Instant createdAt;

    public ClientEntity() {
    }

    public ClientEntity(UUID id,
                        String email,
                        String password,
                        String apiKey,
                        int rateLimit,
                        int userLimit,
                        int rateWindowSeconds,
                        boolean enabled,
                        Instant createdAt) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.apiKey = apiKey;
        this.rateLimit = rateLimit;
        this.userLimit = userLimit;
        this.rateWindowSeconds = rateWindowSeconds;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(int rateLimit) {
        this.rateLimit = rateLimit;
    }

    public long getRateWindowSeconds() {
        return rateWindowSeconds;
    }

    public void setRateWindowSeconds(long rateWindowSeconds) {
        this.rateWindowSeconds = rateWindowSeconds;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(int userLimit) {
        this.userLimit = userLimit;
    }

    @Override
    public String toString() {
        return "ClientEntity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", rateLimit=" + rateLimit +
                ", rateWindowSeconds=" + rateWindowSeconds +
                ", isActive=" + enabled +
                ", createdAt=" + createdAt +
                '}';
    }
}
