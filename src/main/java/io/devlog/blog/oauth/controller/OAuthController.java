package io.devlog.blog.oauth.controller;

import io.devlog.blog.oauth.service.OAuthServiceImpl;
import io.devlog.blog.user.DTO.OAuthDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// To-do
// 1. accessToken 으로 OAuth2.0 쪽에 user-info 요청
// 2. 유저 db에 있는 유니크값과 비교해서 db에 존재 유무 확인
// 3. 존재하지 않으면 회원가입
// 4. 존재하면 로그인
// 4.1 로그인 됐을 경우 - accessToken, refreshToken, 사용자 정보 전송
// 자체 제작 accessToken 과 refreshToken 발급처리

@Log4j2
@RestController
@RequestMapping(value = "/oauth")
public class OAuthController {
    private final OAuthServiceImpl oAuthServiceImpl;

    public OAuthController(OAuthServiceImpl oAuthServiceImpl) {
        this.oAuthServiceImpl = oAuthServiceImpl;
    }

    /***
     * Naver OAuth2.0 Login
     * @return NaverInfo
     */
    @PostMapping("/naver")
    public ResponseEntity<?> callBack(@RequestBody OAuthDTO oauthDTO) {
        log.info("naver : {}", oauthDTO);
        return oAuthServiceImpl.loginOf(oauthDTO.getCode(), oauthDTO.getState(), "naver");
    }

    /***
     * Google OAuth2.0 Login
     * @return GoogleInfo
     */
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody OAuthDTO oauthDTO) {
        log.info("google : {}", oauthDTO);
        return oAuthServiceImpl.loginOf(oauthDTO.getCode(), oauthDTO.getState(), "google");
    }

    /***
     * GitHub OAuth2.0 Login
     * @return GithubInfo
     */
    @PostMapping("/github")
    public ResponseEntity<?> githubLogin(@RequestBody OAuthDTO oauthDTO) {
        log.info("github : {}", oauthDTO);
        return oAuthServiceImpl.loginOf(oauthDTO.getCode(), oauthDTO.getState(), "github");
    }

    @GetMapping("/refresh")
    public String tokenRefresh(@ModelAttribute("refresh_token") String refreshToken) {
        log.info("refresh token : {}", refreshToken);
        return "refresh";
    }
}