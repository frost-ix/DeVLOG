package io.devlog.blog.user.DTO;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthInfoDTO {
    String email;
    String uuid;

    public OAuthInfoDTO(String email, String uuid) {
        this.email = email;
        this.uuid = uuid;
    }
}
