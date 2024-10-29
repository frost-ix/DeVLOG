package io.devlog.blog.team.dto;

import io.devlog.blog.team.entity.TeamRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamRoleDTO {
    private String teamRoleName;
    private long userUuid;
    private long tUuid;

    public TeamRole toEntity() {
        return TeamRole.builder()
                .teamRoleName(teamRoleName)
                .userUuid(userUuid)
                .build();
    }
}
