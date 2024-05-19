package io.devlog.blog.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccessRole {
    CLIENT("ROLE_CLIENT", "사용자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    UNKNOWN("ROLE_UNKNOWN", "익명");

    private final String key;
    private final String role;
}
