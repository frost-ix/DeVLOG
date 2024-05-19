package io.devlog.blog.user.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtToken {
    private String accessToken;
    private String refreshToken;
}
