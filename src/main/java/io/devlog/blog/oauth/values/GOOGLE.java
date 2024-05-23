package io.devlog.blog.oauth.values;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GOOGLE {
    //    Registration Area
    @Value("${spring.security.oauth2.client.registration.google.client-name}")
    private String GOOGLE_CLIENT_NAME;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String GOOGLE_AUTHORIZATION_GRANT_TYPE;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;
    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String GOOGLE_SCOPE;
    //    Provider Area
    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String GOOGLE_AUTHORIZATION_URI;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String GOOGLE_TOKEN_URI;
    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String GOOGLE_USER_INFO_URI;
    @Value("${spring.security.oauth2.client.provider.google.user-name-attribute}")
    private String GOOGLE_USER_NAME_ATTRIBUTE;
}
