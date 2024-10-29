package io.devlog.blog.team.dto;

import lombok.Data;

@Data
public class TBlogDTO {
    private String tDomain;
    private String tTitle;
    private String tName;
    private String tSubject;

    public TBlogDTO(String tDomain, String tTitle, String tName, String tSubject) {
        this.tDomain = tDomain;
        this.tTitle = tTitle;
        this.tName = tName;
        this.tSubject = tSubject;
    }

    public static TBlogDTO of(String tDomain, String tTitle, String tName, String tSubject) {
        return new TBlogDTO(tDomain, tTitle, tName, tSubject);
    }

    public static TBlogDTO of(String tDomain, String tTitle, String tName) {
        return new TBlogDTO(tDomain, tTitle, tName, null);
    }
}
