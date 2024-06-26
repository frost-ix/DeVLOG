package io.devlog.blog.oauth.DTO;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class GoogleDTO implements OAuth2Info {
    Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
