package io.devlog.blog.user.service;

import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.DTO.JwtToken;
import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.repository.SubscribesRepository;
import io.devlog.blog.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SubscribesRepository subscribesRepository;
    private final PasswordEncoder pwEncoder;
    private final JwtService jwtService;
    private final HttpServletResponse httpServletResponse;

    public UserServiceImpl(final UserRepository userRepository, final SubscribesRepository subscribesRepository, PasswordEncoder pwEncoder, JwtService jwtService, HttpServletResponse httpServletResponse) {
        this.userRepository = userRepository;
        this.subscribesRepository = subscribesRepository;
        this.pwEncoder = pwEncoder;
        this.jwtService = jwtService;
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public ResponseEntity<?> getUsers() {
        try {
            List<User> finds = userRepository.findAll();
            if (finds.isEmpty()) {
                log.error("No user");
                return ResponseEntity.notFound().build();
            } else {
                for (User f : finds) {
                    log.info("Get users : {}", f.getRoleKey());
                    log.info("Get users : {}", f.getAccessRole());
                }
                return ResponseEntity.ok().body(finds);
            }
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public ResponseEntity<?> login(UserDTO user) {
        try {
            Optional<User> find = userRepository.findOneByBenderUuid(user.getBenderUuid());
            if (find.isEmpty()) {
                log.info("No account");
                return ResponseEntity.notFound().build();
            }
            if (!pwEncoder.matches(user.getPw(), find.get().getUserPw())) {
                log.info("Password not match");
                return ResponseEntity.status(401).body("Password not match");
            } else {
                log.info("Login success");
                String accessToken = jwtService.createAccessToken(find.get().getBenderUuid());
                JwtToken jwtToken = new JwtToken(accessToken, jwtService.createRefreshToken(find.get().getBenderUuid(), accessToken));
                httpServletResponse.addHeader("Authorization", jwtToken.getAccessToken());
                Cookie cookie = new Cookie("refreshToken", jwtToken.getRefreshToken());
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 24);
                httpServletResponse.addCookie(cookie);
                return ResponseEntity.ok().body("Login success. check cookie");
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<?> create(final UserDTO user) {
        try {
            log.info("Creating user: {}", user);
            if (userRepository.findOneByBenderUuid(user.getBenderUuid()).isPresent()) {
                log.error("Already exist user: {}", user);
                return ResponseEntity.status(409).body("Already exist user");
            } else {
                user.setPw(pwEncoder.encode(user.getPw()));
                User check = userRepository.save(user.toEntity());
                UserDTO returnData = user.toDTO(check);
                log.info("Created user: {}", returnData);
                return ResponseEntity.ok().body(returnData);
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public UserDTO update(UserDTO user) {
        try {
            log.info("Updating user: {}", user);
            Optional<User> find = userRepository.findOneByBenderUuid(user.getBenderUuid());
            if (find.isEmpty()) {
                log.error("No user: {}", user);
                return null;
            } else {
                User update = find.get();
                Long uuid = update.getUserUuId();
//                userRepository.updateUserByUserUuid(uuid, user.toEntity());
//                User check = userRepository.findOneByUserId(user.getId()).get();
                /*UserDTO returnData = user.toDTO(check);
                log.info("Updated user: {}", returnData);
                return returnData;*/
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResponseEntity<String> deleteUser(String benderUuid) {
        try {
            log.info("Deleting user: {}", benderUuid);
            User user = userRepository.findOneByBenderUuid(benderUuid).get();
            userRepository.deleteById(user.getUserUuId());
            return ResponseEntity.ok().body("Delete user");
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.notFound().build();
        }
    }
}