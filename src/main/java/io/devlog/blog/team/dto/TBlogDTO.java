package io.devlog.blog.team.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TBlogDTO {
    private Long userUuid;

    public static TBlogDTO of(Long userUuid) {
        return new TBlogDTO(userUuid);
    }
}
