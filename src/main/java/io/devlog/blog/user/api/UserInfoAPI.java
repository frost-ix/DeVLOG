package io.devlog.blog.user.api;

import io.devlog.blog.user.DTO.UserInfoDTO;
import io.devlog.blog.user.service.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>User info API</h1>
 * <ul>Handling user information</ul>
 * <ul>GET /i/{userUuid} : Get user information</ul>
 * <ul>POST /i/{userUuid} : Update user information</ul>
 */

@RestController
@RequestMapping("/user/i")
public class UserInfoAPI {
    private final UserInfoService userInfoService;

    public UserInfoAPI(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/{userUuid}")
    public ResponseEntity<?> getUserInfo(@PathVariable long userUuid) {
        return userInfoService.getUserInfo(userUuid);
    }

    @PostMapping("/{userUuid}")
    public ResponseEntity<?> updateUserInfo(@PathVariable long userUuid, @RequestBody UserInfoDTO info) {
        return userInfoService.updateUserInfo(userUuid, info);
    }
}
