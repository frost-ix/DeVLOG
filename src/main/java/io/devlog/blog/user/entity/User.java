package io.devlog.blog.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.devlog.blog.pblog.Entity.PBlog;
import io.devlog.blog.team.entity.TeamBlog;
import io.devlog.blog.user.enums.AccessRole;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userUuid")
    private long userUuid;

    @Column(name = "userId")
    private String userId;
    @Column(name = "userPw")
    private String userPw;
    @Nullable
    @Column(name = "oName")
    private String bender;
    @Column(name = "oUuid")
    private String benderUuid;
    private String name;
    private String mail;
    @Enumerated(EnumType.STRING)
    private AccessRole accessRole;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userUuid")
    @JsonManagedReference
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Subscribes> subscribes;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonManagedReference
    private PBlog pbLog;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "teamUuid")
    private TeamBlog team;

    public String getRoleKey() {
        return this.accessRole.getKey();
    }

    public Long getUserUuid() {
        return this.userUuid;
    }
}