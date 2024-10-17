package io.devlog.blog.user.service;

import io.devlog.blog.user.DTO.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> getUsers();

    ResponseEntity<?> getUser(String name, long userUuid);

    ResponseEntity<?> login(UserDTO user);

    ResponseEntity<?> create(UserDTO user);

    ResponseEntity<?> update(long userUuid, UserDTO user);

    ResponseEntity<String> deleteUser(long userUuid);

    ResponseEntity<?> passwordCheck(long userUuid, String password);
}