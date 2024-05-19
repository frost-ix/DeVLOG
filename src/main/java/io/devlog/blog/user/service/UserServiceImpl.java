package io.devlog.blog.user.service;

import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder pwEncoder;

    public UserServiceImpl(final UserRepository userRepository, PasswordEncoder pwEncoder) {
        this.userRepository = userRepository;
        this.pwEncoder = pwEncoder;
    }

    @Override
    public List<User> getUsers() {
        List<User> finds = userRepository.findAll();
        if (finds.isEmpty()) {
            log.error("No user");
            return null;
        } else {
            log.info("Get users : {}", finds);
            return finds;
        }
    }

    @Override
    public Map<String, Boolean> login(final String userId, final String userPw) {
        try {
            log.info("Entry userId : {}", userId);
            Optional<User> find = userRepository.findOneById(userId);
            if (find.isEmpty()) {
                log.info("No account");
                return Map.of();
            }
            boolean check = pwEncoder.matches(userPw, find.get().getPw());
            log.info("Encoded check : {}", check);
            if (!check) {
                log.info("Password not match");
                return Map.of("login", false);
            } else {
                log.info("Login success : {}", find.get());
                return Map.of("login", true);
            }
        } catch (Exception e) {
            log.error(e);
            return Map.of("error", true);
        }
    }

    @Override
    public UserDTO create(final UserDTO user) {
        try {
            log.info("Creating user: {}", user);
            if (userRepository.findOneById(user.getId()).isPresent()) {
                log.error("Already exist user: {}", user);
                return null;
            } else {
                String hP = pwEncoder.encode(user.getPw());
                log.info("Encoded password: {}", hP);
                User save = user.toEntity();
                User check = userRepository.save(save);
                UserDTO returnData = user.toDTO(check);
                log.info("Created user: {}", returnData);
                return returnData;
            }
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public UserDTO update(UserDTO user) {
        try {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteUser(String userId) {
        try {
            userRepository.deleteById(userId);
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }
}
