package io.devlog.blog.user.api;

import io.devlog.blog.user.DTO.SubscribesDTO;
import io.devlog.blog.user.service.UserSubService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>User Subscribe API</h1>
 * <p>
 * User Subscribe API is a REST API that provides the following functionalities:
 *  <ul>
 *      <li>Get all user subscribes</li> GET /user/s/all
 *      <li>Get user subscribe count</li> GET /user/s/count
 *      <li>Get user subscribe</li> GET /user/s
 *      <li>Add user subscribe</li> POST /user/s
 *      <li>Delete user subscribe</li> DELETE /user/s
 *      <li>Delete user subscribes</li> DELETE /user/s/all
 * </ul>
 * <p>
 * <b>Note:</b> This API is only accessible to the user with the role of CLIENT.
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
    @GetMapping("/all")
    public ResponseEntity<?> getUsersSub() {
        log.info("test");
        return userSubService.getUsersSub();
    }

    /**
     * <h1>Get user subscribe count</h1>
     * <ul>GET /user/s/{userUuid}/count : Get user subscribe count</ul>
     *
     * @return ResponseEntity<?> ? User Subscribe count : Error
     */
    @GetMapping("/count")
    public ResponseEntity<?> getUsersSubCount() {
        log.info("All Subscribe count");
        return userSubService.getUsersSubCount();
    }


    /**
     * <h1>Get user subscribe</h1>
     * <ul>GET /user/s/{userUuid} : Get user subscribe</ul>
     *
     * @return ResponseEntity<?> ? User Subscribes : Error
     */
    @GetMapping("")
    public ResponseEntity<?> getUserSub() {
        return userSubService.getUserSub();
    }

    /**
     * <h1>Add user subscribe</h1>
     * <ul>POST /user/s/{userUuid} : Add user subscribe</ul>
     *
     * @param sbDTO SubscribesDTO
     * @return ResponseEntity<?> ? Success : Error
     */
    @PostMapping("")
    public ResponseEntity<?> addUserSub(@RequestBody SubscribesDTO sbDTO) {
        return userSubService.addUserSub(sbDTO);
    }

    /**
     * <h1>Delete user subscribe</h1>
     * <ul>DELETE /user/s/{userUuid} : Delete user subscribe</ul>
     *
     * @param sbDTO SubscribesDTO
     * @return ResponseEntity<?> ? Success : Error
     */
    @DeleteMapping("")
    public ResponseEntity<?> deleteUserSub(@RequestBody SubscribesDTO sbDTO) {
        return userSubService.deleteUserSub(sbDTO);
    }

    /**
     * <h1>Delete user subscribes</h1>
     * <ul>DELETE /user/s : Delete user subscribes</ul>
     *
     * @return ResponseEntity<?> ? Success : Error
     */
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteUserSubs() {
        return userSubService.deleteUserSubs();
    }


}
