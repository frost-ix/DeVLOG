package io.devlog.blog.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.DTO.JwtToken;
import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.config.CustomException;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.enums.ErrorStatus;
import io.devlog.blog.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class UserServiceImpl extends QuerydslRepositorySupport implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder pwEncoder;
    private final JwtService jwtService;
    private final HttpServletResponse httpServletResponse;

    private final JPAQueryFactory jpaqf;

    public UserServiceImpl(final UserRepository userRepository, PasswordEncoder pwEncoder,
                           JwtService jwtService, HttpServletResponse httpServletResponse,
                           JPAQueryFactory jpaqf) {
        super(User.class);
        this.userRepository = userRepository;
        this.pwEncoder = pwEncoder;
        this.jwtService = jwtService;
        this.httpServletResponse = httpServletResponse;
        this.jpaqf = jpaqf;
    }

    /***
     * Get all users
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> getUsers() {
        try {
            List<User> finds = userRepository.findAll();
            if (finds.isEmpty()) {
                log.error("No user");
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok().body(finds);
            }
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    /***
     * Get user by name
     * @param name user name
     * @param userUuid user uuid
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> getUser(String name, long userUuid) {
        try {
            log.info("Get user : {}", name);
            Optional<User> find = userRepository.findOneByUserUuid(userUuid);
            if (find.isEmpty()) {
                log.error("Not found user");
                return ResponseEntity.badRequest().body(new CustomException(ErrorStatus.USER_NOT_FOUND));
            } else {
                return ResponseEntity.ok().body(find.get());
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    /***
     * Login check
     * @param user UserDTO (id, pw)
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> login(UserDTO user) {
        try {
            Optional<User> find = userRepository.findOneByUserId(user.getId());
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
                User r = find.get();
                return ResponseEntity.status(200).body(r);
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    /***
     * Create user
     * @param user UserDTO (id, pw, bender, benderUuid, name, mail)
     * @return ResponseEntity
     */
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
                UserDTO returnData = UserDTO.toDTO(check);
                log.info("Created user: {}", returnData);
                return ResponseEntity.ok().body(returnData);
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    /***
     * Update user
     * @param userUuid user uuid
     * @param user UserDTO (id, pw, bender, benderUuid, name, mail)
     * @return ResponseEntity
     */
    @Override
    @Transactional
    public ResponseEntity<?> update(long userUuid, UserDTO user) {
        try {
            if (userRepository.findOneByUserUuid(userUuid).isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                String pw = pwEncoder.encode(user.getPw());
                user.setPw(pw);
                int e = userRepository.updateUserByUserUuid(userUuid, pw, user.getName(), user.getMail());
                if (e == 0) {
                    return ResponseEntity.badRequest().body("Update fail");
                }
                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Check Exception in server");
        }
    }

    /***
     * Delete user
     * @param userUuid user uuid
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<String> deleteUser(long userUuid) {
        try {
            log.info("Deleting user: {}", userUuid);
            Optional<User> user = userRepository.findOneByUserUuid(userUuid);
            if (user.isEmpty()) {
                log.error("No user.\nBenderUuid : {}", userUuid);
                return ResponseEntity.notFound().build();
            } else {
                userRepository.deleteById(user.get().getUserUuId());
            }
            return ResponseEntity.ok().body("Delete ok");
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.notFound().build();
        }
    }

    /***
     * Check password
     * @param userUuid user uuid
     * @param password password
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> passwordCheck(long userUuid, String password) {
        try {
            if (userRepository.findOneByUserUuid(userUuid).isPresent()) {
                return ResponseEntity.ok(pwEncoder.matches(password, userRepository.findOneByUserUuid(userUuid).get().getUserPw()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
    }
}