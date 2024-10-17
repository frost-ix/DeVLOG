package io.devlog.blog.user.api;

import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/user")
public class UserAPI {
    private final UserService userService;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getUser(@PathVariable String name, @RequestBody long userUuid) {
        return userService.getUser(name, userUuid);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO user) {
        log.info("Entry : {}", user);
        return userService.login(user);
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserDTO user) {
        log.info(user);
        return userService.create(user);
    }

    @PatchMapping("/{userUuid}")
    public ResponseEntity<?> updateUser(@PathVariable long userUuid, @RequestBody UserDTO user) {
        log.info(user);
        return userService.update(userUuid, user);
    }

    @DeleteMapping("/{userUuid}")
    public ResponseEntity<String> deleteUser(@PathVariable long userUuid) {
        return userService.deleteUser(userUuid);
    }

    @PostMapping("/{userUuid}/check")
    public ResponseEntity<?> passwordCheck(@PathVariable long userUuid, @RequestBody String password) {
        return userService.passwordCheck(userUuid, password);
    }
}