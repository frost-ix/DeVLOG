package io.devlog.blog.team.dto;

import io.devlog.blog.team.entity.TBlog;
import io.devlog.blog.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TBlogDTO {
    private long tUuid;
    private String tDomain;
    private String tTitle;
    private String tName;
    private String tSubject;
    private String tInfo;

    public static TBlogDTO of(long tUuid, String tDomain, String tTitle, String tName, String tSubject, String tInfo) {
        return new TBlogDTO(tUuid, tDomain, tTitle, tName, tSubject, tInfo);
    }

    public static TBlogDTO of(String tDomain, String tTitle, String tName, String tSubject, String tInfo) {
        return new TBlogDTO(0, tDomain, tTitle, tName, tSubject, tInfo);
    }

    public TBlog toEntity(User user) {
        return TBlog.builder()
                .user(user)
                .tDomain(tDomain)
                .tTitle(tTitle)
                .tName(tName)
                .tSubject(tSubject)
                .build();
    }
}
