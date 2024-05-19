package io.devlog.blog.user.service;

import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> getUsers();

    Map<String, Boolean> login(String userId, String userPw);

    UserDTO create(UserDTO user);

    UserDTO update(UserDTO user);

    boolean deleteUser(String userId);
}
