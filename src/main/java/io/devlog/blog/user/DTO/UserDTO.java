package io.devlog.blog.user.DTO;

import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.enums.AccessRole;
import io.devlog.blog.user.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDTO {
    @NonNull
    private String id;
    private String pw;
    private String name;
    private String mail;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Enumerated(EnumType.STRING)
    private AccessRole accessRole;

    @Builder
    public UserDTO(@NonNull String id, @NonNull String pw, String name, String mail) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.mail = mail;
        this.userRole = UserRole.PERSONAL_USER;
        this.accessRole = AccessRole.CLIENT;
    }

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .pw(user.getPw())
                .name(user.getName())
                .mail(user.getMail())
                .build();
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .pw(pw)
                .name(name)
                .mail(mail)
                .userRole(userRole)
                .accessRole(accessRole)
                .build();
    }
}
