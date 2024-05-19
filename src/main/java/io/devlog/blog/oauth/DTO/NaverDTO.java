package io.devlog.blog.oauth.DTO;

import java.util.Map;

public class NaverDTO implements OAuth2Info {
    private final Map<String, Object> attributes;

    public NaverDTO(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("sub"));
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("name"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }
}
