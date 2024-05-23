package io.devlog.blog.user.entity;

import io.devlog.blog.user.enums.AccessRole;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userUuid;

    @Nullable
    private String userId;
    @Nullable
    private String userPw;
    @Nullable
    @Column(name = "oName")
    private String bender;

    private String name;
    private String mail;
    @Enumerated(EnumType.STRING)
    private AccessRole accessRole;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info_userUuid")
    private UserInfo userInfo;

    public User update(String name, String mail) {
        this.name = name;
        this.mail = mail;
        return this;
    }

    public String getRoleKey() {
        return this.accessRole.getKey();
    }
}
