package io.devlog.blog.oauth.values;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GITHUB {
    //    Registration Area
    @Value("${spring.security.oauth2.client.registration.github.client-name}")
    private String GITHUB_CLIENT_NAME;
    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String GITHUB_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String GITHUB_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String GITHUB_REDIRECT_URI;
    @Value("${spring.security.oauth2.client.registration.github.authorization-grant-type}")
    private String GITHUB_AUTHORIZATION_GRANT_TYPE;
    @Value("${spring.security.oauth2.client.registration.github.scope}")
    private String GITHUB_SCOPE;
    //   Provider Area
    @Value("${spring.security.oauth2.client.provider.github.authorization-uri}")
    private String GITHUB_AUTHORIZATION_URI;
    @Value("${spring.security.oauth2.client.provider.github.token-uri}")
    private String GITHUB_TOKEN_URI;
    @Value("${spring.security.oauth2.client.provider.github.user-info-uri}")
    private String GITHUB_USER_INFO_URI;
    @Value("${spring.security.oauth2.client.provider.github.user-name-attribute}")
    private String GITHUB_USER_NAME_ATTRIBUTE;
}
