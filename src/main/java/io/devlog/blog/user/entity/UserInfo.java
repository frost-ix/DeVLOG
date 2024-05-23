package io.devlog.blog.user.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_info")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userUuid;
    @Nullable
    private String userIcon;
    @Nullable
    private String userInfo;
    @Nullable
    private String userGit;
    @Nullable
    private String userX;
    @Nullable
    private String userInsta;
}
