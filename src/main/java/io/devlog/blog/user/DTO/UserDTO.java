package io.devlog.blog.user.DTO;

import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.enums.AccessRole;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDTO {
    private UserInfoDTO userInfo;
    private String id;
    private String pw;
    private String bender;
    private String benderUuid;
    private String name;
    private String mail;
    @Enumerated(EnumType.STRING)
    private AccessRole accessRole;

    // Normal Create
    @Builder
    public UserDTO(@Nullable String id, @Nullable String pw,
                   @Nullable String bender, @Nullable String benderUuid,
                   @Nullable String name, @Nullable String mail) {
        this.id = id;
        this.pw = pw;
        this.bender = bender;
        this.benderUuid = benderUuid;
        this.name = name;
        this.mail = mail;
        this.accessRole = AccessRole.CLIENT;
    }

    // Normal Create
    @Builder
    public UserDTO(@Nullable String id, @Nullable String pw,
                   @Nullable String bender, @Nullable String benderUuid,
                   @Nullable String name, @Nullable String mail,
                   @Nullable UserInfoDTO userInfo) {
        this.id = id;
        this.pw = pw;
        this.bender = bender;
        this.benderUuid = benderUuid;
        this.name = name;
        this.mail = mail;
        this.accessRole = AccessRole.CLIENT;
        this.userInfo = userInfo;
    }

    @Builder
    public UserDTO(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    // Entity to DTO
    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(Objects.requireNonNull(user.getUserId()))
                .pw(Objects.requireNonNull(user.getUserPw()))
                .bender(user.getBender())
                .benderUuid(user.getBenderUuid())
                .name(user.getName())
                .mail(user.getMail())
                .build();
    }

    public static UserDTO toDTO(User user, UserInfoDTO userInfo) {
        return UserDTO.builder()
                .id(Objects.requireNonNull(user.getUserId()))
                .pw(Objects.requireNonNull(user.getUserPw()))
                .bender(user.getBender())
                .benderUuid(user.getBenderUuid())
                .name(user.getName())
                .mail(user.getMail())
                .build();
    }

    // DTO to Entity
    public User toEntity() {
        return User.builder()
                .userId(id)
                .userPw(pw)
                .bender(bender)
                .benderUuid(benderUuid)
                .name(name)
                .mail(mail)
                .accessRole(accessRole)
                .build();
    }
}