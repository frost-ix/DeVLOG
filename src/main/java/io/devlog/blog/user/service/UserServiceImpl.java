package io.devlog.blog.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.devlog.blog.config.CustomException;
import io.devlog.blog.config.ResponseCheck;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.DTO.JwtToken;
import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    /**
     * Get all users
     *
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> getUsers() {
        try {
            List<User> finds = userRepository.findAll();
            if (finds.isEmpty()) {
                log.error("No user");
                throw new CustomException(Status.USER_NOT_FOUND);
            } else {
                return ResponseEntity.ok().body(finds);
            }
        } catch (Exception e) {
            log.error(e);
            throw new CustomException(Status.BAD_REQUEST);
        }
    }

    /**
     * Login check
     *
     * @param user UserDTO (id, pw)
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> login(UserDTO user) {
        try {
            Optional<User> find = userRepository.findOneByUserId(user.getId());
            if (find.isEmpty()) {
                log.info("No account");
                throw new CustomException(Status.USER_NOT_FOUND);
            }
            if (!pwEncoder.matches(user.getPw(), find.get().getUserPw())) {
                log.info("Password not match");
                throw new CustomException(Status.UNAUTHORIZED);
            } else {
                log.info("Login success");
                setCookie(find);
                User r = find.get();
                UserDTO u = UserDTO.toDTO(r);
                u.setPw(null);
                u.setBenderUuid(null);
                u.setBender(null);
                return ResponseEntity.status(200).body(u);
            }
        } catch (Exception e) {
            log.error(e);
            throw new CustomException(Status.BAD_REQUEST);
        }
    }

    /**
     * <h1>Description</h1>
     * <li>setCookie method</li>
     * <li>caution : Don't put non-optional object in parameter</li>
     *
     * @param u Optional User
     */
    private void setCookie(Optional<User> u) {
        if (u.isPresent()) {
            String accessToken = jwtService.createAccessToken(u.get().getBenderUuid());
            JwtToken jwtToken = new JwtToken(accessToken, jwtService.createRefreshToken(u.get().getBenderUuid(), accessToken));
            httpServletResponse.addHeader("Authorization", jwtToken.getAccessToken());
            Cookie cookie = new Cookie("refreshToken", jwtToken.getRefreshToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24);
            httpServletResponse.addCookie(cookie);
        }
    }

    /**
     * Create user
     *
     * @param user UserDTO (id, pw, bender, benderUuid, name, mail)
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> create(final UserDTO user) {
        try {
            log.info("Creating user: {}", user);
            if (user.getId() == null || user.getPw() == null || user.getBender() == null || user.getBenderUuid() == null || user.getName() == null || user.getMail() == null) {
                log.error("Invalid user data");
                throw new CustomException(Status.BAD_REQUEST);
            }
            if (userRepository.findOneByBenderUuid(user.getId()).isPresent()) {
                log.error("사용중인 ID : {}", user);
                throw new CustomException(Status.CONFLICT);
            } else {
                user.setPw(pwEncoder.encode(user.getPw()));
                User check = userRepository.save(user.toEntity());
                UserDTO returnData = UserDTO.toDTO(check);
                log.info("Created user: {}", returnData);
                return ResponseEntity.ok().body(new ResponseCheck(Status.CREATED));
            }
        } catch (Exception e) {
            log.error(e);
            throw new CustomException(Status.BAD_REQUEST);
        }
    }

    /**
     * Update user
     *
     * @param userUuid user uuid
     * @param user     UserDTO (id, pw, bender, benderUuid, name, mail)
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> update(long userUuid, UserDTO user) {
        try {
            if (userRepository.findOneByUserUuid(userUuid).isEmpty()) {
                log.error("User not exist");
                throw new CustomException(Status.USER_NOT_FOUND);
            } else {
                String pw = pwEncoder.encode(user.getPw());
                user.setPw(pw);
                int e = userRepository.updateUserByUserUuid(userUuid, pw, user.getName(), user.getMail());
                if (e == 0) {
                    log.error("Update failed");
                    throw new CustomException(Status.NOT_MODIFIED);
                }
                return ResponseEntity.ok().body(new ResponseCheck(Status.OK));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(Status.BAD_REQUEST);
        }
    }

    /***
     * Delete user
     * @param userUuid user uuid
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> deleteUser(long userUuid) {
        try {
            log.info("Deleting user: {}", userUuid);
            Optional<User> user = userRepository.findOneByUserUuid(userUuid);
            if (user.isEmpty()) {
                log.error("Already deleted user");
                return ResponseEntity.badRequest().body(new CustomException(Status.USER_NOT_FOUND));
            } else {
                userRepository.deleteById(user.get().getUserUuId());
            }
            return ResponseEntity.ok().body(new ResponseCheck(Status.OK));
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().body(new CustomException(Status.BAD_REQUEST));
        }
    }

    /**
     * Check password
     *
     * @param userUuid user uuid
     * @param password password
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> passwordCheck(long userUuid, String password) {
        try {
            if (userRepository.findOneByUserUuid(userUuid).isPresent()) {
                if (pwEncoder.matches(password, userRepository.findOneByUserUuid(userUuid).get().getUserPw())) {
                    log.info("Password match");
                    return ResponseEntity.ok().body(new ResponseCheck(Status.OK));
                } else {
                    log.error("Password not match");
                    throw new CustomException(Status.UNAUTHORIZED);
                }
            }
            throw new CustomException(Status.USER_NOT_FOUND);
        } catch (Exception e) {
            log.error(e);
            throw new CustomException(Status.BAD_REQUEST);
        }
    }
}