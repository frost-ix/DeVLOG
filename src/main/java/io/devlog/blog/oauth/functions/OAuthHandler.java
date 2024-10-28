package io.devlog.blog.oauth.functions;

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
import io.devlog.blog.user.DTO.OAuthInfoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class OAuthHandler {

    private NAVER naver;
    private GOOGLE google;
    private GITHUB github;

    public OAuthInfoDTO loginNaver(String code, String state, NAVER naver) {
        this.naver = naver;
        ResponseEntity<String> oauthTokenRes = getOAuthToken(code, state, naver.getNAVER_TOKEN_URI(), "naver");
        log.info("Naver token : {}", oauthTokenRes.getBody());
        ObjectMapper tokenObject = new ObjectMapper();
        NaverTokenDTO naverTokenDTO;
        NaverInfo naverInfo;
        try {
            naverTokenDTO = tokenObject.readValue(oauthTokenRes.getBody(), NaverTokenDTO.class);
            String accessToken = naverTokenDTO.getAccessToken();
            ResponseEntity<String> userInfo = getUserInfo(accessToken, naver.getNAVER_USER_INFO_URI(), "naver");
            naverInfo = tokenObject.readValue(userInfo.getBody(), NaverInfo.class);
            log.info("NAVER : {}", naverInfo.getId());
            return new OAuthInfoDTO(naverInfo.getEmail(), naverInfo.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Naver login failed");
        }
    }

    public OAuthInfoDTO loginGoogle(String code, String state, GOOGLE google) {
        this.google = google;
        ResponseEntity<String> oauthTokenRes = getOAuthToken(code, state, google.getGOOGLE_AUTHORIZATION_URI(), "google");
        log.info("Google token : {}", oauthTokenRes.getBody());
        ObjectMapper tokenObject = new ObjectMapper();
        GoogleTokenDTO googleTokenDTO = null;
        GoogleInfo googleInfo = null;
        try {
            googleTokenDTO = tokenObject.readValue(oauthTokenRes.getBody(), GoogleTokenDTO.class);
            String accessToken = googleTokenDTO.getAccessToken();
            ResponseEntity<String> userInfo = getUserInfo(accessToken, google.getGOOGLE_USER_INFO_URI(), "google");
            googleInfo = tokenObject.readValue(userInfo.getBody(), GoogleInfo.class);
            log.info("GOOGLE : {}", googleInfo.getSub());
            return new OAuthInfoDTO(googleInfo.getEmail(), googleInfo.getSub());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Google login failed");
        }
    }

    public OAuthInfoDTO loginGithub(String code, String state, GITHUB github) {
        this.github = github;
        ResponseEntity<String> oauthTokenRes = getOAuthToken(code, state, github.getGITHUB_TOKEN_URI(), "github");
        log.info("Github token : {}", oauthTokenRes.getBody());
        ObjectMapper tokenObject = new ObjectMapper();
        GithubTokenDTO githubTokenDTO = null;
        GithubInfo githubInfo = null;
        try {
            githubTokenDTO = tokenObject.readValue(oauthTokenRes.getBody(), GithubTokenDTO.class);
            String accessToken = githubTokenDTO.getAccessToken();
            ResponseEntity<String> userInfo = getUserInfo(accessToken, github.getGITHUB_USER_INFO_URI(), "github");
            log.info("User info : {}", userInfo.getBody());
            githubInfo = tokenObject.readValue(userInfo.getBody(), GithubInfo.class);
            log.info("GITHUB : {}", githubInfo.getId());
            return new OAuthInfoDTO(githubInfo.getEmail(), githubInfo.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Github login failed");
        }
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
                headers.set("Content-Type", "application/x-www-form-urlencoded");
                params.add("response_type", code);
                params.add("redirect_uri", google.getGOOGLE_REDIRECT_URI());
                params.add("client_id", google.getGOOGLE_CLIENT_ID());
                params.add("client_secret", google.getGOOGLE_CLIENT_SECRET());
                params.add("scope", google.getGOOGLE_SCOPE());
                params.add("grant_type", google.getGOOGLE_AUTHORIZATION_GRANT_TYPE());
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
        log.info("Params : {}", params.values());
        return new HttpEntity<>(params, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> getHeadersEntity(String accessToken, String providerName) {
        HttpHeaders headers = new HttpHeaders();
        log.info("Headers entity accessToken : {}", accessToken);
        log.info("Provider : {}", providerName);
        String tokenType = "Bearer";
        headers.set("Authorization", tokenType + " " + accessToken);
        log.info("Headers : {}", headers);
        return new HttpEntity<>(headers);
    }

    private ResponseEntity<String> getOAuthToken(String code, String state, String tokenUri, String providerName) {
        RestTemplate rst = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> tokenReq = getHttpParamsEntity(code, state, providerName);
        log.info("Code, State, Provider name : {}, {}, {}", code, state, providerName);
        log.info("Token uri : {}", tokenUri);
        log.info("Token request : {}", tokenReq);
        ResponseEntity<String> response = rst.exchange(
                tokenUri,
                HttpMethod.POST,
                tokenReq,
                String.class);
        log.info("Response : {}", response);
        return response;
    }

    private ResponseEntity<String> getUserInfo(String accessToken, String userInfoUri, String providerName) {
        RestTemplate rst = new RestTemplate();
        log.info("accessToken : {}", accessToken);
        HttpEntity<MultiValueMap<String, String>> headers = getHeadersEntity(accessToken, providerName);
        return rst.exchange(
                userInfoUri,
                HttpMethod.GET,
                headers,
                String.class);
    }
}
