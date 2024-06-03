package io.devlog.blog.oauth.service;

import io.devlog.blog.user.DTO.JwtToken;

public interface OAuthService {
    JwtToken loginOf(String code, String state, String providerName);
}