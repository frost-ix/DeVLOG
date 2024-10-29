package io.devlog.blog.team.dto;

import io.devlog.blog.team.entity.TBlog;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TBlogDTO {
    private String tDomain;
    private String tBanner;
    private String tName;

    private String tInfo;

    public TBlog toEntity() {
        return TBlog.builder()
                .tDomain(tDomain)
                .tBanner(tBanner)
                .tName(tName)
                .tInfo(tInfo)
                .build();
    }
}
