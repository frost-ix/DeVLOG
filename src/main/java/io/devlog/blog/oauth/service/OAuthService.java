package io.devlog.blog.oauth.service;

import org.springframework.http.ResponseEntity;

public interface OAuthService {
    ResponseEntity<?> loginOf(String code, String state, String providerName);
}