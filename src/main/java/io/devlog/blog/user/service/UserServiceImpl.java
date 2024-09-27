package io.devlog.blog.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.DTO.JwtToken;
import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.config.CustomException;
import io.devlog.blog.user.entity.QUser;
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
                }
                return ResponseEntity.ok().body(finds);
            }
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

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
    @Transactional
    public ResponseEntity<?> update(UserDTO user) {
        try {
            QUser q = QUser.user;
            log.info("user : {}", user);
            User find = jpaqf.select(q)
                    .from(q)
                    .where(q.benderUuid.eq(user.getBenderUuid()))
                    .fetchOne();
            if (find == null) {
                return ResponseEntity.notFound().build();
            } else {
                log.info("Updating user: {}", user);
                long check = jpaqf.update(q)
                        .set(q.mail, user.getMail())
                        .where(q.benderUuid.eq(user.getBenderUuid()))
                        .execute();
                log.info("update : {}", check);
                return ResponseEntity.ok().body("Test field");
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().body("Update fail");
        }
    }

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
}