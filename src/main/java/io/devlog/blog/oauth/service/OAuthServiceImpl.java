package io.devlog.blog.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.devlog.blog.oauth.DTO.Token.GithubTokenDTO;
import io.devlog.blog.oauth.DTO.Token.GoogleTokenDTO;
import io.devlog.blog.oauth.DTO.Token.NaverTokenDTO;
import io.devlog.blog.oauth.DTO.info.GithubInfo;
import io.devlog.blog.oauth.DTO.info.GoogleInfo;
import io.devlog.blog.oauth.DTO.info.NaverInfo;
import io.devlog.blog.oauth.values.GITHUB;
import io.devlog.blog.oauth.values.GOOGLE;
import io.devlog.blog.oauth.values.NAVER;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.DTO.JwtToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class OAuthServiceImpl implements OAuthService {
    private final NAVER naver;
    private final GOOGLE google;
    private final GITHUB github;

    private final JwtService jwtService;

    @Value("Bearer")
    private String tokenType;

    public OAuthServiceImpl(NAVER naver, GOOGLE google, GITHUB github, JwtService jwtService) {
        this.naver = naver;
        this.google = google;
        this.github = github;
        this.jwtService = jwtService;
    }

    public JwtToken loginOf(String code, String state, String providerName) {
        switch (providerName) {
            case "naver" -> {
                return loginNaver(code, state);
            }
            case "google" -> {
                return loginGoogle(code, state);
            }
            case "github" -> {
                return loginGithub(code, state);
            }
            default -> throw new RuntimeException("Provider not found");
        }
    }

    private JwtToken loginNaver(String code, String state) {
        ResponseEntity<String> oauthTokenRes = getOAuthToken(code, state, "naver");
        ObjectMapper tokenObject = new ObjectMapper();
        NaverTokenDTO naverTokenDTO;
        NaverInfo naverInfo;
        try {
            naverTokenDTO = tokenObject.readValue(oauthTokenRes.getBody(), NaverTokenDTO.class);
            String accessToken = naverTokenDTO.getAccessToken();
            ResponseEntity<String> userInfo = getUserInfo(accessToken, "naver");
            naverInfo = tokenObject.readValue(userInfo.getBody(), NaverInfo.class);
            log.info("NAVER : {}", naverInfo.getId());
            return createToken(naverInfo.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Naver login failed");
        }
    }

    private JwtToken loginGoogle(String code, String state) {
        // To-do
        ResponseEntity<String> oauthTokenRes = getOAuthToken(code, state, "google");
        ObjectMapper tokenObject = new ObjectMapper();
        GoogleTokenDTO googleTokenDTO = null;
        GoogleInfo googleInfo = null;
        try {
            googleTokenDTO = tokenObject.readValue(oauthTokenRes.getBody(), GoogleTokenDTO.class);
            String accessToken = googleTokenDTO.getAccessToken();
            ResponseEntity<String> userInfo = getUserInfo(accessToken, "google");
            googleInfo = tokenObject.readValue(userInfo.getBody(), GoogleInfo.class);
            log.info("GOOGLE : {}", googleInfo.getSub());
            return createToken(googleInfo.getSub());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Google login failed");
        }
    }

    private JwtToken loginGithub(String code, String state) {
        ResponseEntity<String> oauthTokenRes = getOAuthToken(code, state, "github");
        ObjectMapper tokenObject = new ObjectMapper();
        GithubTokenDTO githubTokenDTO = null;
        GithubInfo githubInfo = null;
        try {
            githubTokenDTO = tokenObject.readValue(oauthTokenRes.getBody(), GithubTokenDTO.class);
            String accessToken = githubTokenDTO.getAccessToken();
            ResponseEntity<String> userInfo = getUserInfo(accessToken, "github");
            githubInfo = tokenObject.readValue(userInfo.getBody(), GithubInfo.class);
            log.info("GITHUB : {}", githubInfo.getId());
            return createToken(githubInfo.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Github login failed");
        }
    }

    private JwtToken createToken(String id) {
        String accessToken = jwtService.createAccessToken(id);
        JwtToken jwtToken = new JwtToken(jwtService.createAccessToken(id), jwtService.createRefreshToken(id, accessToken));
        log.info(jwtToken);
        return jwtToken;
    }

    private HttpEntity<MultiValueMap<String, String>> getHttpParamsEntity(String code, String state, String providerName) {
        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        switch (providerName) {
            case "naver" -> {
                headers.set("Content-Type", "application/x-www-form-urlencoded");
                params.add("grant_type", naver.getNAVER_AUTHORIZATION_GRANT_TYPE());
                params.add("client_id", naver.getNAVER_CLIENT_ID());
                params.add("client_secret", naver.getNAVER_CLIENT_SECRET());
                params.add("code", code);
                params.add("state", state);
            }
            case "google" -> {
                params.add("code", code);
                params.add("client_id", google.getGOOGLE_CLIENT_ID());
                params.add("client_secret", google.getGOOGLE_CLIENT_SECRET());
                params.add("redirect_uri", google.getGOOGLE_REDIRECT_URI());
                params.add("grant_type", google.getGOOGLE_AUTHORIZATION_GRANT_TYPE());
                params.add("state", state);
            }
            case "github" -> {
                headers.set("Accept", "application/json");
                params.add("client_id", github.getGITHUB_CLIENT_ID());
                params.add("client_secret", github.getGITHUB_CLIENT_SECRET());
                params.add("code", code);
                params.add("redirect_uri", github.getGITHUB_REDIRECT_URI());
                params.add("state", state);
            }
            default -> throw new RuntimeException("Provider not found");
        }
        return new HttpEntity<>(params, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> getHeadersEntity(String accessToken, String providerName) {
        HttpHeaders headers = new HttpHeaders();
        switch (providerName) {
            case "naver", "google" -> {
                headers.set("Content-Type", "application/x-www-form-urlencoded");
                headers.set("Authorization", tokenType + " " + accessToken);
            }
            case "github" -> {
                headers.set("Accept", "application/json");
                headers.set("Authorization", tokenType + " " + accessToken);
            }
            default -> throw new RuntimeException("Provider not found");
        }
        return new HttpEntity<>(headers);
    }

    private ResponseEntity<String> getOAuthToken(String code, String state, String providerName) {
        RestTemplate rst = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> tokenReq = getHttpParamsEntity(code, state, providerName);
        return rst.exchange(
                switch (providerName) {
                    case "naver" -> naver.getNAVER_TOKEN_URI();
                    case "google" -> google.getGOOGLE_TOKEN_URI();
                    case "github" -> github.getGITHUB_TOKEN_URI();
                    default -> throw new RuntimeException("Provider not found");
                },
                HttpMethod.POST,
                tokenReq,
                String.class);
    }

    private ResponseEntity<String> getUserInfo(String accessToken, String providerName) {
        RestTemplate rst = new RestTemplate();
        return rst.exchange(
                switch (providerName) {
                    case "naver" -> naver.getNAVER_USER_INFO_URI();
                    case "google" -> google.getGOOGLE_USER_INFO_URI();
                    case "github" -> github.getGITHUB_USER_INFO_URI();
                    default -> throw new RuntimeException("Provider not found");
                },
                HttpMethod.GET,
                getHeadersEntity(accessToken, providerName),
                String.class);
    }
}