package io.devlog.blog.user.api;

import io.devlog.blog.user.DTO.UserInfoDTO;
import io.devlog.blog.user.service.UserInfoService;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
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
     * @return ResponseEntity<?> ? User Information : Error
     */
    @GetMapping("")
    public ResponseEntity<?> getUserInfo() {
        return userInfoService.getUserInfo();
    }

    /**
     * <h1>Update user information</h1>
     * <ul>POST /user/i/{userUuid} : Update user information</ul>
     *
     * @param info UserInfoDTO
     * @return ResponseEntity<?> ? Success : Error
     */
    @PostMapping("")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserInfoDTO info) {
        return userInfoService.updateUserInfo(info);
    }
}
