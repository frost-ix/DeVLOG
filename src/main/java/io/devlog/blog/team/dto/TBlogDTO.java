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
    private String tBanner;
    private String tInfo;

    public static TBlogDTO of(long tUuid, String tDomain, String tTitle, String tName, String tSubject, String tInfo, String tBanner) {
        return new TBlogDTO(tUuid, tDomain, tTitle, tName, tSubject, tInfo, tBanner);
    }

    public static TBlogDTO of(String tDomain, String tTitle, String tName, String tSubject, String tInfo, String tBanner) {
        return new TBlogDTO(0, tDomain, tTitle, tName, tSubject, tInfo, tBanner);
    }

    public static TBlogDTO of(long tUuid) {
        return new TBlogDTO(tUuid, null, null, null, null, null, null);
    }

    public static TBlogDTO toDTO(TBlog tBlog) {
        return TBlogDTO.of(
                tBlog.getTUuid(), tBlog.getTDomain(),
                tBlog.getTTitle(), tBlog.getTName(),
                tBlog.getTSubject(), tBlog.getTInfo(), tBlog.getTBanner()
        );
    }

    public TBlog toEntity(User user) {
        return TBlog.builder()
                .user(user)
                .tDomain(tDomain)
                .tTitle(tTitle)
                .tName(tName)
                .tSubject(tSubject)
                .tBanner(tBanner)
                .build();
    }
}
