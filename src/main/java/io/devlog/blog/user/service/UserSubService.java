package io.devlog.blog.user.service;

import io.devlog.blog.user.DTO.SubscribesDTO;
import org.springframework.http.ResponseEntity;

public interface UserSubService {
    ResponseEntity<?> getUsersSub();

    ResponseEntity<?> getUsersSubCount(long userUuid);

    ResponseEntity<?> getUserSub(long subUuid);

    ResponseEntity<?> addUserSub(long userUuid, SubscribesDTO sbDTO);

    ResponseEntity<?> deleteUserSub(long subUuid);

    ResponseEntity<?> deleteUserSubs(long userUuid);
}
