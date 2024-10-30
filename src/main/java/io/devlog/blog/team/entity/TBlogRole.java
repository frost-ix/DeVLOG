package io.devlog.blog.team.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tblog_role")
@Builder(toBuilder = true)
public class TBlogRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tRoleUuid;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "tUuid")
    @JsonBackReference
    private TBlog tBlog;

    private String teamRole;

    private String userIcon;

    private String memberDescription;

    @Column(unique = true)
    private long userUuid;
}
