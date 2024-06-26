package io.devlog.blog.user.DTO;

import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.enums.AccessRole;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDTO {
    @Nullable
    private String id;
    @Nullable
    private String pw;
    private String bender;
    private String benderUuid;
    private String name;
    private String mail;
    @Enumerated(EnumType.STRING)
    private AccessRole accessRole;

    @Builder
    public UserDTO(@NonNull String id, @NonNull String pw, String bender, String benderUuid, String name, String mail) {
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