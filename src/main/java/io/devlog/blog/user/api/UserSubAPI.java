package io.devlog.blog.user.api;

import io.devlog.blog.user.DTO.SubscribesDTO;
import io.devlog.blog.user.service.UserSubService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/sub")
public class UserSubAPI {
    private final UserSubService userSubService;

    public UserSubAPI(UserSubService userSubService) {
        this.userSubService = userSubService;
    }

    @GetMapping("")
    public ResponseEntity<?> getUserSubs() {
        log.info("test");
        return userSubService.getUsersSub();
    }

    @GetMapping("/{userUuid}/count")
    public ResponseEntity<?> getUsersSubCount(@PathVariable long userUuid) {
        log.info("All Subscribe count");
        return userSubService.getUsersSubCount(userUuid);
    }


    @GetMapping("/{userUuid}")
    public ResponseEntity<?> getUserSub(@PathVariable long userUuid) {
        return userSubService.getUserSub(userUuid);
    }

    @PostMapping("/{userUuid}")
    public ResponseEntity<?> addUserSub(@PathVariable long userUuid, @RequestBody SubscribesDTO sbDTO) {
        return userSubService.addUserSub(userUuid, sbDTO);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUserSubs(@RequestBody long userUuid) {
        return userSubService.deleteUserSubs(userUuid);
    }

    @DeleteMapping("/{subUuid}")
    public ResponseEntity<?> deleteUserSub(@PathVariable long subUuid) {
        return userSubService.deleteUserSub(subUuid);
    }

}
