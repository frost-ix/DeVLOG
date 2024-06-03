package io.devlog.blog.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @OneToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "userUuid")
    private User user;
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