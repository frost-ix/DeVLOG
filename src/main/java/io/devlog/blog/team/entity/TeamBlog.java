package io.devlog.blog.team.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tblog")
@Builder(toBuilder = true)
public class TeamBlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_uuid")
    private Long teamUuid;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userUuid")
    private User user;

    @OneToMany(mappedBy = "tblog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Categories> categories;

    @OneToOne(mappedBy = "tblog", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonManagedReference
    private TeamRole teamRole;

    private String tDomain;
    private String tBanner;
    private String tName;
}
