package io.devlog.blog.user.api;

import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/user")
public class UserAPI {
    private final UserService userService;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/auth")
    public ResponseEntity<?> auth() {
        return ResponseEntity.ok().body("auth");
    }

    @GetMapping("")
    public ResponseEntity<?> getUsers() {
        List<User> userList = userService.getUsers();
        if (userList.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.ok().body(userList);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> login(@PathVariable String userId, @ModelAttribute UserDTO user) {
        log.info("Entry : {}", user);
        Map<String, Boolean> check = userService.login(userId, user.getPw());
        log.info(check);
        if (!check.isEmpty()) {
            if (check.get("login")) {
                return ResponseEntity.ok().body(check);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(check);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<String> createUser(@Validated @ModelAttribute UserDTO user) {
        log.info(user);
        UserDTO find = userService.create(user);
        if (find == null) {
            return ResponseEntity.badRequest().body("Already exist user !");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(find.getId());
        }
    }

    @PutMapping("{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @ModelAttribute User user) {
        log.info(user);
        return ResponseEntity.accepted().body(user);
    }

    @DeleteMapping("{userId}")
    public boolean deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }
}
