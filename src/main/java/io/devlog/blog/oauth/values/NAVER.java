package io.devlog.blog.oauth.values;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NAVER {
    // Registration Area
    @Value("${spring.security.oauth2.client.registration.naver.client-name")
    private String NAVER_CLIENT_NAME;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;
    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String NAVER_AUTHORIZATION_GRANT_TYPE;
    @Value("${spring.security.oauth2.client.registration.naver.client-authentication-method}")
    private String NAVER_CLIENT_AUTHENTICATION_METHOD;
    // Provider Area
    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String NAVER_AUTHORIZATION_URI;
    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String NAVER_TOKEN_URI;
    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String NAVER_USER_INFO_URI;
    @Value("${spring.security.oauth2.client.provider.naver.user-name-attribute")
    private String NAVER_USER_NAME_ATTRIBUTE;
}