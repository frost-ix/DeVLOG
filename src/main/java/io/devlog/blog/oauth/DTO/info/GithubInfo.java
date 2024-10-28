package io.devlog.blog.oauth.DTO.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubInfo {
    @JsonProperty("node_id")
    private String node_id;
    private String id;
    private String name;
}
