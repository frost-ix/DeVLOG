package io.devlog.blog.oauth.controller;

import io.devlog.blog.oauth.service.OAuthServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController("/api/oauth")
public class OAuthController {
    private final OAuthServiceImpl oAuthServiceImpl;

    public OAuthController(OAuthServiceImpl oAuthServiceImpl) {
        this.oAuthServiceImpl = oAuthServiceImpl;
    }

    @GetMapping("/naver")
    public void trustTest(@RequestParam("code") String code, @RequestParam("state") String state) {
        log.info("code : {}, state : {}", code, state);
        System.out.println("code : " + code);
    }

    @GetMapping("/naver/token")
    public ResponseEntity<?> getToken(@RequestParam String code, @RequestParam String state) {
        return ResponseEntity.ok().body(oAuthServiceImpl.getSocialTokens(code));
    }

    @GetMapping("/github")
    public ResponseEntity<?> githubSign() {
        return null;
    }
}
