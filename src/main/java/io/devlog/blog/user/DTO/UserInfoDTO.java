package io.devlog.blog.user.DTO;

import io.devlog.blog.user.entity.UserInfo;
import lombok.Getter;

@Getter
public class UserInfoDTO {
    private final String userIcon;
    private final String userInfo;
    private final String userGit;
    private final String userX;
    private final String userInsta;

    public UserInfoDTO(String userIcon, String userInfo, String userGit, String userX, String userInsta) {
        this.userIcon = userIcon;
        this.userInfo = userInfo;
        this.userGit = userGit;
        this.userX = userX;
        this.userInsta = userInsta;
    }

    public UserInfo toEntity() {
        return UserInfo.builder()
                .userIcon(userIcon)
                .userInfo(userInfo)
                .userGit(userGit)
                .userX(userX)
                .userInsta(userInsta)
                .build();
    }
}
