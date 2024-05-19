package io.devlog.blog.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    GUEST("ROLE_GUEST", "비회원"),
    PERSONAL_USER("ROLE_P_USER", "개인 사용자"),
    TEAM_LEADER("ROLE_T_LEADER", "팀 블로그 팀장"),
    TEAM_MEMBER("ROLE_T_MEMBER", "팀 블로그 팀원");

    private final String key;
    private final String role;
}
