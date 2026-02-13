package com.household.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.cookie")
public record CookieProperties (
  boolean secure
){}
