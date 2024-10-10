package io.devlog.blog.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "userUuid")
    @JsonManagedReference
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Subscribes> subscribes;

    public User update(String name, String mail) {
        this.name = name;
        this.mail = mail;
        return this;
    }

    public String getRoleKey() {
        return this.accessRole.getKey();
    }

    public Long getUserUuId() {
        return this.userUuid;
    }
}