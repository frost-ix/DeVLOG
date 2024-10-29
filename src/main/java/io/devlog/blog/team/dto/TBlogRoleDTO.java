package io.devlog.blog.team.dto;

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
}