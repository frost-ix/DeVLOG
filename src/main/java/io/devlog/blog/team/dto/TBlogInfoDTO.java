package io.devlog.blog.team.dto;

import io.devlog.blog.team.entity.TBlogInfo;
import lombok.Data;

@Data
public class TBlogInfoDTO {
    private String tDomain;
    private String tBanner;
    private String tName;
    private String tInfo;

    private TBlogInfoDTO(String tDomain, String tBanner, String tName, String tInfo) {
        this.tDomain = tDomain;
        this.tBanner = tBanner;
        this.tName = tName;
        this.tInfo = tInfo;
    }

    public static TBlogInfoDTO of(String tDomain, String tBanner, String tName, String tInfo) {
        return new TBlogInfoDTO(tDomain, tBanner, tName, tInfo);
    }

    public TBlogInfo toEntity() {
        return TBlogInfo.builder()
                .tDomain(tDomain)
                .tBanner(tBanner)
                .tName(tName)
                .tInfo(tInfo)
                .build();
    }
}