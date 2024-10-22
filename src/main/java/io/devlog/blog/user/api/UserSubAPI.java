package io.devlog.blog.user.api;

import io.devlog.blog.user.DTO.SubscribesDTO;
import io.devlog.blog.user.service.UserSubService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>User subscribe API</h1>
 * <ul>Handling user subscribes</ul>
 * <ul>GET /user/s : Get all user subscribes</ul>
 * <ul>GET /user/s/{userUuid}/count : Get user subscribe count</ul>
 * <ul>GET /user/s/{userUuid} : Get user subscribe</ul>
 * <ul>POST /user/s/{userUuid} : Add user subscribe</ul>
 * <ul>DELETE /user/s/{userUuid} : Delete user subscribe</ul>
 * <ul>DELETE /user/s : Delete user subscribes</ul>
 *
 * @see io.devlog.blog.user.service.UserSubServiceImpl
 * @see io.devlog.blog.user.DTO.SubscribesDTO
 */
@Log4j2
@RestController
@RequestMapping("/user/s")
public class UserSubAPI {
    private final UserSubService userSubService;

    public UserSubAPI(UserSubService userSubService) {
        this.userSubService = userSubService;
    }

    /**
     * <h1>Get usr Subscribes</h1>
     * <ul>GET /user/s : Get all user subscribes</ul>
     *
     * @return ResponseEntity<?> ? User Subscribes : Error
     */
    @GetMapping("")
    public ResponseEntity<?> getUserSubs() {
        log.info("test");
        return userSubService.getUsersSub();
    }

    /**
     * <h1>Get user subscribe count</h1>
     * <ul>GET /user/s/{userUuid}/count : Get user subscribe count</ul>
     *
     * @param userUuid user's uuid
     * @return ResponseEntity<?> ? User Subscribe count : Error
     */
    @GetMapping("/{userUuid}/count")
    public ResponseEntity<?> getUsersSubCount(@PathVariable long userUuid) {
        log.info("All Subscribe count");
        return userSubService.getUsersSubCount(userUuid);
    }


    /**
     * <h1>Get user subscribe</h1>
     * <ul>GET /user/s/{userUuid} : Get user subscribe</ul>
     *
     * @param userUuid user's uuid
     * @return ResponseEntity<?> ? User Subscribes : Error
     */
    @GetMapping("/{userUuid}")
    public ResponseEntity<?> getUserSub(@PathVariable long userUuid) {
        return userSubService.getUserSub(userUuid);
    }

    /**
     * <h1>Add user subscribe</h1>
     * <ul>POST /user/s/{userUuid} : Add user subscribe</ul>
     *
     * @param userUuid user's uuid
     * @param sbDTO    SubscribesDTO
     * @return ResponseEntity<?> ? Success : Error
     */
    @PostMapping("/{userUuid}")
    public ResponseEntity<?> addUserSub(@PathVariable long userUuid, @RequestBody SubscribesDTO sbDTO) {
        return userSubService.addUserSub(userUuid, sbDTO);
    }

    /**
     * <h1>Delete user subscribe</h1>
     * <ul>DELETE /user/s/{userUuid} : Delete user subscribe</ul>
     *
     * @param userUuid user's uuid
     * @param sbDTO    SubscribesDTO
     * @return ResponseEntity<?> ? Success : Error
     */
    @DeleteMapping("/{userUuid}")
    public ResponseEntity<?> deleteUserSub(@PathVariable long userUuid, @RequestBody SubscribesDTO sbDTO) {
        log.info("Access : {}", userUuid);
        return userSubService.deleteUserSub(sbDTO);
    }

    /**
     * <h1>Delete user subscribes</h1>
     * <ul>DELETE /user/s : Delete user subscribes</ul>
     *
     * @param userUuid user's uuid
     * @return ResponseEntity<?> ? Success : Error
     */
    @DeleteMapping("")
    public ResponseEntity<?> deleteUserSubs(@RequestBody SubscribesDTO userUuid) {
        return userSubService.deleteUserSubs(userUuid);
    }


}
