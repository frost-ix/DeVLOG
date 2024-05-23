package io.devlog.blog.oauth.DTO.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubInfo {
    private String id;
    private String name;
    private String node_id;
    private String avatar_url;
}
