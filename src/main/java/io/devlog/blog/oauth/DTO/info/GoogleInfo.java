package io.devlog.blog.oauth.DTO.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleInfo {
    private String sub;
    private String name;
    private String picture;
    private String email;
}
