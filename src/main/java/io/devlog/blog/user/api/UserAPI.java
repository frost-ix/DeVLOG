package io.devlog.blog.user.api;

import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.entity.User;
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

    @GetMapping("")
    public ResponseEntity<?> login(@ModelAttribute UserDTO user) {
        log.info("Entry : {}", user);
        return userService.login(user);
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@Validated @ModelAttribute UserDTO user) {
        log.info(user);
        return userService.create(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable String userId, @ModelAttribute User user) {
        log.info(user);
        return ResponseEntity.ok().body("after update");
    }

    @DeleteMapping("/{benderUuid}")
    public ResponseEntity<String> deleteUser(@PathVariable String benderUuid) {
        return userService.deleteUser(benderUuid);
    }
}