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

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "tUuid")
    @JsonBackReference
    private TBlog tBlog;

    private String teamRole;

    private String userIcon;

    private String memberDescription;
    
    private long userUuid;
}
