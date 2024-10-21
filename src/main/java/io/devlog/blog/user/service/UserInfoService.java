package io.devlog.blog.user.service;

import io.devlog.blog.user.DTO.UserInfoDTO;
import org.springframework.http.ResponseEntity;

public interface UserInfoService {
    ResponseEntity<?> getUserInfo(long userUuid);

    ResponseEntity<?> updateUserInfo(long userUuid, UserInfoDTO info);
}
