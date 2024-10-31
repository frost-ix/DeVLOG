package io.devlog.blog.team.dto;

import io.devlog.blog.team.entity.TBlog;
import io.devlog.blog.team.entity.TBlogRole;
import lombok.Data;

@Data
public class TBlogRoleDTO {
    private Long userUuid;
    private Long tUuid;
    private String userName;
    private String role;
    private String userIcon;
    private String description;

    public TBlogRoleDTO(Long userUuid, Long tUuid,
                        String role, String userIcon,
                        String description, String userName) {
        this.userUuid = userUuid;
        this.tUuid = tUuid;
        this.role = role;
        this.userIcon = userIcon;
        this.description = description;
        this.userName = userName;
    }

    public static TBlogRoleDTO of(Long userUuid, Long tUuid, String role, String userIcon, String description) {
        return new TBlogRoleDTO(userUuid, tUuid, role, userIcon, description, null);
    }

    public static TBlogRoleDTO of(Long userUuid, Long tUuid, String role, String userIcon) {
        return new TBlogRoleDTO(userUuid, tUuid, role, userIcon, null, null);
    }

    public static TBlogRoleDTO of(String role, String userIcon, String description, String userName) {
        return new TBlogRoleDTO(null, null, role, userIcon, description, userName);
    }

    public TBlogRole toEntity(TBlogRoleDTO tBlogRoleDTO, TBlog tBlog) {
        return TBlogRole.builder()
                .tBlog(tBlog)
                .teamRole(tBlogRoleDTO.getRole())
                .userIcon(tBlogRoleDTO.getUserIcon())
                .memberDescription(tBlogRoleDTO.getDescription())
                .userUuid(tBlogRoleDTO.getUserUuid())
                .build();
    }
}