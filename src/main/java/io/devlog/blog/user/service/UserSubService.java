package io.devlog.blog.user.service;

import io.devlog.blog.user.DTO.SubscribesDTO;
import org.springframework.http.ResponseEntity;

public interface UserSubService {
    ResponseEntity<?> getUsersSub();

    ResponseEntity<?> getUserSub(long subUuid);

    ResponseEntity<?> addUserSub(SubscribesDTO sbDTO);

    ResponseEntity<?> deleteUserSub(long subUuid);

    ResponseEntity<?> deleteUserSubs(long userUuid);
}
