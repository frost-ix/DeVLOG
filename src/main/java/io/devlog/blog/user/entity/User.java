package io.devlog.blog.user.entity;

import io.devlog.blog.user.enums.AccessRole;
import io.devlog.blog.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "user_info")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_uuid;

    private String id;
    private String pw;
    private String name;
    private String mail;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Enumerated(EnumType.STRING)
    private AccessRole accessRole;

    public User update(String name, String mail) {
        this.name = name;
        this.mail = mail;
        return this;
    }

    public String getRoleKey() {
        return this.accessRole.getKey();
    }
}
