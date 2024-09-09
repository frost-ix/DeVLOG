package io.devlog.blog.oauth.service;

public interface OAuthService {
    String loginOf(String code, String state, String providerName);
}