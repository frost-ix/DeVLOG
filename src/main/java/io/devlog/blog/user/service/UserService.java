package io.devlog.blog.user.service;

import io.devlog.blog.user.DTO.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> getUsers();

    ResponseEntity<?> login(UserDTO user);

    ResponseEntity<?> login(String refreshToken);

    ResponseEntity<?> logout();

    ResponseEntity<?> create(UserDTO user);

    ResponseEntity<?> update(long userUuid, UserDTO user);

    ResponseEntity<?> deleteUser(long userUuid);

    ResponseEntity<?> passwordCheck(long userUuid, String password);
}