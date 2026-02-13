package com.household.backend.dto.res;

public record LoginRes(UserRes user, String accessToken) {}
