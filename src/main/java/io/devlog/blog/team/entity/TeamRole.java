package io.devlog.blog.team.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team_role")
@Builder(toBuilder = true)
public class TeamRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_role_uuid")
    private Long teamRoleUuid;

    @Column(name = "team_role")
    private String teamRoleName;

    @Column(name = "team_member")
    private long userUuid;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "team_uuid")
    private TeamBlog team;
}
