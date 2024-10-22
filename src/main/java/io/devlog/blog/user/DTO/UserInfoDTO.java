package io.devlog.blog.user.DTO;

import io.devlog.blog.user.entity.UserInfo;
import lombok.Getter;

@Getter
public class UserInfoDTO {
    private final String userIcon;
    private final String userSummary;
    private final String userGit;
    private final String userX;
    private final String userInsta;

    public UserInfoDTO(String userIcon, String userSummary, String userGit, String userX, String userInsta) {
        this.userIcon = userIcon;
        this.userSummary = userSummary;
        this.userGit = userGit;
        this.userX = userX;
        this.userInsta = userInsta;
    }

    public UserInfo toEntity() {
        return UserInfo.builder()
                .userIcon(userIcon)
                .userSummary(userSummary)
                .userGit(userGit)
                .userX(userX)
                .userInsta(userInsta)
                .build();
    }
}