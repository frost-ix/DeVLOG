package io.devlog.blog.oauth.DTO.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverInfo {
    private Response response;

    private String resultcode;
    private String message;

    public String getId() {
        return response.getId();
    }

    public String getName() {
        return response.getNickname();
    }

    public String getEmail() {
        return response.getEmail();
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Response {
        private String id;
        private String nickname;
        private String email;
    }
}
