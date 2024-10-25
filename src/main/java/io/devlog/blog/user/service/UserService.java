package io.devlog.blog.user.service;

import io.devlog.blog.user.DTO.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> getUsers();

    ResponseEntity<?> login(UserDTO user);

    ResponseEntity<?> login(String refreshToken);

    ResponseEntity<?> logout();

    ResponseEntity<?> create(UserDTO user);

    ResponseEntity<?> update(UserDTO user);

    ResponseEntity<?> deleteUser();

    ResponseEntity<?> passwordCheck(String password);
}