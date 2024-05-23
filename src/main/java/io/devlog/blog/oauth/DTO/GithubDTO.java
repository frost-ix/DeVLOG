package io.devlog.blog.oauth.DTO;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class GithubDTO implements OAuth2Info {
    private final Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "github";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
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
