package io.devlog.blog.user.api;

import io.devlog.blog.user.DTO.UserInfoDTO;
import io.devlog.blog.user.service.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>User info API</h1>
 * <ul>Handling user information</ul>
 * <ul>GET /user/i/{userUuid} : Get user information</ul>
 * <ul>POST /user/i/{userUuid} : Update user information</ul>
 *
 * @see io.devlog.blog.user.service.UserInfoServiceImpl
 * @see io.devlog.blog.user.DTO.UserInfoDTO
 */

@RestController
@RequestMapping("/user/i")
public class UserInfoAPI {
    private final UserInfoService userInfoService;

    public UserInfoAPI(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * <h1>Get user information</h1>
     * <ul>GET /user/i/{userUuid} : Get user information</ul>
     *
     * @param userUuid user's uuid
     * @return ResponseEntity<?> ? User Information : Error
     */
    @GetMapping("/{userUuid}")
    public ResponseEntity<?> getUserInfo(@PathVariable long userUuid) {
        return userInfoService.getUserInfo(userUuid);
    }

    /**
     * <h1>Update user information</h1>
     * <ul>POST /user/i/{userUuid} : Update user information</ul>
     *
     * @param userUuid user's uuid
     * @param info     UserInfoDTO
     * @return ResponseEntity<?> ? Success : Error
     */
    @PostMapping("/{userUuid}")
    public ResponseEntity<?> updateUserInfo(@PathVariable long userUuid, @RequestBody UserInfoDTO info) {
        return userInfoService.updateUserInfo(userUuid, info);
    }
}
