package io.devlog.blog.team.dto;

import io.devlog.blog.team.entity.TBlog;
import io.devlog.blog.team.entity.TBlogRole;
import lombok.Data;

@Data
public class TBlogRoleDTO {
    private Long userUuid;
    private Long tUuid;
    private String role;
    private String userIcon;
    private String description;

    public TBlogRoleDTO(Long userUuid, Long tUuid, String role, String userIcon, String description) {
        this.userUuid = userUuid;
        this.tUuid = tUuid;
        this.role = role;
        this.userIcon = userIcon;
        this.description = description;
    }

    public static TBlogRoleDTO of(Long userUuid, Long tUuid, String role, String userIcon, String description) {
        return new TBlogRoleDTO(userUuid, tUuid, role, userIcon, description);
    }

    public static TBlogRoleDTO of(Long userUuid, Long tUuid, String role, String userIcon) {
        return new TBlogRoleDTO(userUuid, tUuid, role, userIcon, null);
    }

    public static TBlogRoleDTO of(Long userUuid, Long tUuid, String role) {
        return new TBlogRoleDTO(userUuid, tUuid, role, null, null);
    }

    public TBlogRole toEntity(TBlog tBlog) {
        return TBlogRole.builder()
                .userUuid(userUuid)
                .tBlog(tBlog)
                .teamRole(role)
                .userIcon(userIcon)
                .memberDescription(description)
                .build();
    }
}