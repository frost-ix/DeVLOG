package io.devlog.blog.team.dto;

import io.devlog.blog.team.entity.TBlog;
import io.devlog.blog.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TBlogDTO {
    private String tDomain;
    private String tTitle;
    private String tName;
    private String tSubject;

    public static TBlogDTO of(String tDomain, String tTitle, String tName, String tSubject) {
        return new TBlogDTO(tDomain, tTitle, tName, tSubject);
    }

    public static TBlogDTO of(String tDomain, String tTitle, String tName) {
        return new TBlogDTO(tDomain, tTitle, tName, null);
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
