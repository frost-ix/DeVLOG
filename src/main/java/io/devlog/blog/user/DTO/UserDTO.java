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
    @Nullable
    private String id;
    @Nullable
    private String pw;
    @Nullable
    private String bender;
    @Nullable
    private String benderUuid;
    @Nullable
    private String name;
    @Nullable
    private String mail;
    @Nullable
    @Enumerated(EnumType.STRING)
    private AccessRole accessRole;

    @Builder
    public UserDTO(@Nullable String id, @Nullable String pw, @Nullable String bender, @Nullable String benderUuid, @Nullable String name, @Nullable String mail) {
        this.id = id;
        this.pw = pw;
        this.bender = bender;
        this.benderUuid = benderUuid;
        this.name = name;
        this.mail = mail;
        this.accessRole = AccessRole.CLIENT;
    }

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(Objects.requireNonNull(user.getUserId()))
                .pw(Objects.requireNonNull(user.getUserPw()))
                .bender(user.getBender())
                .benderUuid(user.getBenderUuid())
                .name(user.getName())
                .mail(user.getMail())
                .build();
    }

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