package com.household.backend.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties (
    String secret,
    long accessExpMin,
    long refreshExpDays
){}
