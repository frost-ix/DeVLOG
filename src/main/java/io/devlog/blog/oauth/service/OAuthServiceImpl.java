package io.devlog.blog.oauth.service;

import io.devlog.blog.oauth.functions.OAuthHandler;
import io.devlog.blog.oauth.values.GITHUB;
import io.devlog.blog.oauth.values.GOOGLE;
import io.devlog.blog.oauth.values.NAVER;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class OAuthServiceImpl implements OAuthService {
    private final OAuthHandler oAuthHandler = new OAuthHandler();
    @Autowired
    private GOOGLE google;
    @Autowired
    private GITHUB github;
    @Autowired
    private NAVER naver;

    public ResponseEntity<?> loginOf(String code, String state, String providerName) {
        switch (providerName) {
            case "naver" -> {
                return ResponseEntity.ok().body(oAuthHandler.loginNaver(code, state, naver));
            }
            case "google" -> {
                return ResponseEntity.ok().body(oAuthHandler.loginGoogle(code, state, google));
            }
            case "github" -> {
                return ResponseEntity.ok().body(oAuthHandler.loginGithub(code, state, github));
            }
            default -> throw new RuntimeException("Provider not found");
        }
    }
}