package io.devlog.blog.oauth.service;

import io.devlog.blog.oauth.Attr.OAuthAttributes;
import io.devlog.blog.oauth.DTO.GithubDTO;
import io.devlog.blog.oauth.DTO.NaverDTO;
import io.devlog.blog.oauth.DTO.OAuth2Info;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User : {}", oAuth2User.getAttributes());
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Info oAuth2Info = null;

        switch (provider) {
            case "naver" -> {
                log.info("Naver login :)");
                oAuth2Info = new NaverDTO(oAuth2User.getAttributes());
            }
            case "github" -> {
                log.info("Github login :)");
                oAuth2Info = new GithubDTO(oAuth2User.getAttributes());
            }
            case "google" -> {
                log.info("Google login :)");
                oAuth2Info = new GithubDTO(oAuth2User.getAttributes());
            }
        }
        String registrationId = userRequest
                .getClientRegistration().getRegistrationId();
        String userNameAttribute = userRequest
                .getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes attributes = OAuthAttributes
                .of(registrationId, userNameAttribute, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    public User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findOneById(attributes.getName())
                .map(entity -> entity.update(attributes.getName(), attributes.getEmail()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
