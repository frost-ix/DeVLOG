package io.devlog.blog.user.DTO;

import io.devlog.blog.pblog.DTO.PblogDTO;
import io.devlog.blog.team.dto.TBlogDTO;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.enums.AccessRole;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDTO {
    private UserInfoDTO userInfo;
    private PblogDTO pBlogDTO;
    private List<TBlogDTO> tBlogDTO;
    private String id;
    private String pw;
    private String bender;
    private String benderUuid;
    private String name;
    private String mail;
    private int subCount;
    private boolean sub = false;
    private LocalDateTime subDate = null;
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
    public UserDTO(@Nullable String id,
                   @Nullable String name, @Nullable String mail,
                   @Nullable UserInfoDTO userInfo, @Nullable PblogDTO pBlogDTO) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.accessRole = AccessRole.CLIENT;
        this.userInfo = userInfo;
        this.pBlogDTO = pBlogDTO;
    }

    @Builder
    public UserDTO(@Nullable String id, @Nullable String pw,
                   @Nullable String bender, @Nullable String benderUuid,
                   @Nullable String name, @Nullable String mail,
                   @Nullable PblogDTO pBlogDTO) {
        this.id = id;
        this.pw = pw;
        this.bender = bender;
        this.benderUuid = benderUuid;
        this.name = name;
        this.mail = mail;
        this.accessRole = AccessRole.CLIENT;
        this.pBlogDTO = pBlogDTO;
    }

    @Builder
    public UserDTO(UserDTO userDTO, @Nullable UserInfoDTO userInfo, PblogDTO pBlog, boolean isSub) {
        this.id = userDTO.getId();
        this.name = userDTO.getName();
        this.mail = userDTO.getMail();
        this.subCount = userDTO.getSubCount();
        this.accessRole = AccessRole.CLIENT;
        this.userInfo = userInfo;
        this.pBlogDTO = pBlog;
        this.sub = isSub;
    }

    @Builder
    public UserDTO(UserDTO userDTO, @Nullable UserInfoDTO userInfo,
                   @Nullable PblogDTO pBlogDTO) {
        this.id = userDTO.getId();
        this.name = userDTO.getName();
        this.mail = userDTO.getMail();
        this.accessRole = AccessRole.CLIENT;
        this.userInfo = userInfo;
        this.pBlogDTO = pBlogDTO;
    }

    @Builder
    public UserDTO(UserDTO userDTO, @Nullable UserInfoDTO userInfo,
                   @Nullable PblogDTO pBlogDTO, @Nullable List<TBlogDTO> tBlogDTO) {
        this.id = userDTO.getId();
        this.name = userDTO.getName();
        this.mail = userDTO.getMail();
        this.accessRole = AccessRole.CLIENT;
        this.userInfo = userInfo;
        this.pBlogDTO = pBlogDTO;
        this.tBlogDTO = tBlogDTO;
    }

    @Builder
    public UserDTO(@Nullable String id, @Nullable String pw,
                   @Nullable String bender, @Nullable String benderUuid,
                   @Nullable String name, @Nullable String mail,
                   @Nullable UserInfoDTO userInfo, @Nullable PblogDTO pBlogDTO) {
        this.id = id;
        this.pw = pw;
        this.bender = bender;
        this.benderUuid = benderUuid;
        this.name = name;
        this.mail = mail;
        this.accessRole = AccessRole.CLIENT;
        this.userInfo = userInfo;
        this.pBlogDTO = pBlogDTO;
    }

    @Builder
    public UserDTO(@Nullable String id, @Nullable String pw,
                   @Nullable String bender, @Nullable String benderUuid,
                   @Nullable String name, @Nullable String mail,
                   @Nullable UserInfoDTO userInfo, @Nullable PblogDTO pBlogDTO,
                   @Nullable int subCount) {
        this.id = id;
        this.pw = pw;
        this.bender = bender;
        this.benderUuid = benderUuid;
        this.name = name;
        this.mail = mail;
        this.subCount = subCount;
        this.accessRole = AccessRole.CLIENT;
        this.pBlogDTO = pBlogDTO;
        this.userInfo = userInfo;
    }

    @Builder
    public UserDTO(@Nullable String id, @Nullable String pw,
                   @Nullable String bender, @Nullable String benderUuid,
                   @Nullable String name, @Nullable String mail,
                   @Nullable UserInfoDTO userInfo, @Nullable PblogDTO pBlogDTO,
                   @Nullable int subCount, @Nullable LocalDateTime subDate) {
        this.id = id;
        this.pw = pw;
        this.bender = bender;
        this.benderUuid = benderUuid;
        this.name = name;
        this.mail = mail;
        this.subCount = subCount;
        this.subDate = subDate;
        this.accessRole = AccessRole.CLIENT;
        this.pBlogDTO = pBlogDTO;
        this.userInfo = userInfo;
    }

    @Builder
    public UserDTO(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    @Builder
    public UserDTO(PblogDTO pBlogDTO) {
        this.pBlogDTO = pBlogDTO;
    }

    public UserDTO(UserDTO userDTO, UserInfoDTO dto) {
        this.id = userDTO.getId();
        this.pw = userDTO.getPw();
        this.bender = userDTO.getBender();
        this.benderUuid = userDTO.getBenderUuid();
        this.subCount = userDTO.getSubCount();
        this.name = userDTO.getName();
        this.mail = userDTO.getMail();
        this.accessRole = userDTO.getAccessRole();
        this.userInfo = dto;
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