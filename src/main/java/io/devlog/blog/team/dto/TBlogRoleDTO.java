package io.devlog.blog.team.dto;

import lombok.Data;

@Data
public class TBlogRoleDTO {
    private Long userUuid;
    private Long tUuid;
    private String role;
    private String description;

    private TBlogRoleDTO(Long userUuid, Long tUuid, String role, String description) {
        this.userUuid = userUuid;
        this.tUuid = tUuid;
        this.role = role;
        this.description = description;
    }

    public static TBlogRoleDTO of(Long userUuid, Long tUuid, String role, String description) {
        return new TBlogRoleDTO(userUuid, tUuid, role, description);
    }

    public static TBlogRoleDTO of(Long userUuid, Long tUuid, String role) {
        return new TBlogRoleDTO(userUuid, tUuid, role, null);
    }
}