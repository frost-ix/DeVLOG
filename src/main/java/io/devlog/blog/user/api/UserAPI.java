package io.devlog.blog.user.api;

import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.enums.AccessRole;
import io.devlog.blog.user.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>User API</h1>
 * <ul>Handling user information</ul>
 * <ul>GET /user/list : Get all user list</ul>
 * <ul>POST /user/login : Login</ul>
 * <ul>POST /user : Create User</ul>
 * <ul>PATCH /user/{userUuid} : Update User</ul>
 * <ul>DELETE /user/{userUuid} : Delete User</ul>
 * <ul>POST /user/{userUuid}/check : Check Password</ul>
 *
 * @see io.devlog.blog.user.service.UserServiceImpl
 * @see io.devlog.blog.user.DTO.UserDTO
 * @see io.devlog.blog.user.enums.AccessRole
 */
@Log4j2
@RestController
@RequestMapping("/user")
public class UserAPI {
    private final UserService userService;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    /**
     * <h1>Get User's list</h1>
     * <ul>GET /user/list : Get all user list</ul>
     *
     * @return ResponseEntity<?> ? User List : Error
     */
    @GetMapping("/list")
    public ResponseEntity<?> getUsers() {
        return userService.getUsers();
    }

    /**
     * <h1>Login Method</h1>
     * <ul>POST /user/login : Login</ul>
     *
     * @param user UserDTO
     * @return ResponseEntity<?> ? JWT Token : Error
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO user) {
        log.info("Entry : {}", user);
        return userService.login(user);
    }

    /**
     * <h1>Create User</h1>
     * <ul>POST /user : Create User</ul>
     *
     * @param user UserDTO
     * @return ResponseEntity<?> ? Success : Error
     */
    @PostMapping("")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserDTO user) {
        user.setAccessRole(AccessRole.CLIENT);
        log.info(user);
        return userService.create(user);
    }

    /**
     * <h1>Update User</h1>
     * <ul>PATCH /user/{userUuid} : Update User</ul>
     *
     * @param userUuid User's uuid
     * @param user     UserDTO
     * @return ResponseEntity<?> ? Success : Error
     */
    @PatchMapping("/{userUuid}")
    public ResponseEntity<?> updateUser(@PathVariable long userUuid, @RequestBody UserDTO user) {
        log.info(user);
        return userService.update(userUuid, user);
    }

    /**
     * <h1>Delete User</h1>
     * <ul>DELETE /user/{userUuid} : Delete User</ul>
     *
     * @param userUuid User's uuid
     * @return ResponseEntity<?> ? Success : Error
     */
    @DeleteMapping("/{userUuid}")
    public ResponseEntity<?> deleteUser(@PathVariable long userUuid) {
        return userService.deleteUser(userUuid);
    }

    /**
     * <h1>Check Password</h1>
     * <ul>POST /user/{userUuid}/check : Check Password</ul>
     *
     * @param userUuid User's uuid
     * @param password User's password
     * @return ResponseEntity<?> ? Success : Error
     */
    @PostMapping("/{userUuid}/check")
    public ResponseEntity<?> passwordCheck(@PathVariable long userUuid, @RequestBody String password) {
        return userService.passwordCheck(userUuid, password);
    }
}