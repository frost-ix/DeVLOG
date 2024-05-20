package io.devlog.blog.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.devlog.blog.oauth.DTO.NaverToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.naver.client-authentication-method}")
    private String NAVER_CLIENT_AUTHENTICATION_METHOD;
    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String NAVER_AUTHORIZATION_GRANT_TYPE;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;
    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String NAVER_AUTHORIZATION_URI;
    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String NAVER_TOKEN_URI;
    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String NAVER_USER_INFO_URI;
    @Value("Bearer")
    private String tokenType;

    public NaverToken loginNaver(String code, String state) {
        RestTemplate rst = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> naverTokenReq = getMultiValueMapHttpEntity(code, state);
        ResponseEntity<String> oauthTokenRes = rst
                .exchange(
                        NAVER_TOKEN_URI,
                        org.springframework.http.HttpMethod.POST,
                        naverTokenReq,
                        String.class);
        ObjectMapper tokenObject = new ObjectMapper();
        NaverToken naverToken = null;
        try {
            naverToken = tokenObject.readValue(oauthTokenRes.getBody(), NaverToken.class);
            return naverToken;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Naver login failed");
        }
    }

    private HttpEntity<MultiValueMap<String, String>> getMultiValueMapHttpEntity(String code, String state) {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", NAVER_AUTHORIZATION_GRANT_TYPE);
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("client_secret", NAVER_CLIENT_SECRET);
        params.add("code", code);
        params.add("state", state);

        return new HttpEntity<>(params, headers);
    }
}
