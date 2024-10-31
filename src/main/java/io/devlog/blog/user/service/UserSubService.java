package io.devlog.blog.user.service;

import io.devlog.blog.user.DTO.SubscribesDTO;
import org.springframework.http.ResponseEntity;

public interface UserSubService {
    ResponseEntity<?> getUsersSubCount();

    ResponseEntity<?> getUserSub();

    ResponseEntity<?> addUserSub(SubscribesDTO sbDTO);

    ResponseEntity<?> deleteUserSub(SubscribesDTO sbDTO);

    ResponseEntity<?> deleteUserSubs();
}
