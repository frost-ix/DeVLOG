package io.devlog.blog.team.dto;

import lombok.Data;

@Data
public class TBlogDTO {
    private String tDomain;
    private String tBanner;
    private String tName;
    private String tInfo;

    private TBlogDTO(String tDomain, String tBanner, String tName, String tInfo) {
        this.tDomain = tDomain;
        this.tBanner = tBanner;
        this.tName = tName;
        this.tInfo = tInfo;
    }

    public static TBlogDTO of(String tDomain, String tBanner, String tName, String tInfo) {
        return new TBlogDTO(tDomain, tBanner, tName, tInfo);
    }

    public static TBlogDTO of(String tDomain, String tBanner, String tName) {
        return new TBlogDTO(tDomain, tBanner, tName, null);
    }
}
