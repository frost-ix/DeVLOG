package io.devlog.blog.user.service;

import io.devlog.blog.user.DTO.UserInfoDTO;
import org.springframework.http.ResponseEntity;

public interface UserInfoService {
    ResponseEntity<?> getUserInfo();

    ResponseEntity<?> getPBlogUserInfo(String domain);

    ResponseEntity<?> updateUserInfo(UserInfoDTO info);

    ResponseEntity<?> updateTwitter(UserInfoDTO info);

    ResponseEntity<?> updateGithub(UserInfoDTO info);

    ResponseEntity<?> updateInstagram(UserInfoDTO info);
}
