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
 * <ul>Patch /user/i/{userUuid} : Update user information</ul>
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

    @GetMapping("/{pDomain}")
    public ResponseEntity<?> getPBlogUserInfo(@PathVariable String pDomain) {
        return userInfoService.getPBlogUserInfo(pDomain);
    }

    /**
     * <h1>Update user information</h1>
     * <ul>Patch /user/i/{userUuid} : Update user information</ul>
     *
     * @param info UserInfoDTO
     * @return ResponseEntity<?> ? Success : Error
     */
    @PatchMapping("")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserInfoDTO info) {
        return userInfoService.updateUserInfo(info);
    }

    @PatchMapping("/twitter")
    public ResponseEntity<?> updateTwitter(@RequestBody UserInfoDTO info) {
        return userInfoService.updateTwitter(info);
    }

    @PatchMapping("/github")
    public ResponseEntity<?> updateGithub(@RequestBody UserInfoDTO info) {
        return userInfoService.updateGithub(info);
    }

    @PatchMapping("/instagram")
    public ResponseEntity<?> updateInstagram(@RequestBody UserInfoDTO info) {
        return userInfoService.updateInstagram(info);
    }

    @PatchMapping("/summary")
    public ResponseEntity<?> updateSummary(@RequestBody UserInfoDTO info) {
        return userInfoService.updateSummary(info);
    }

    @PatchMapping("/icon")
    public ResponseEntity<?> updateIcon(@RequestBody UserInfoDTO info) {
        return userInfoService.updateIcon(info);
    }
}
