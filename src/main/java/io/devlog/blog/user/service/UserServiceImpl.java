package io.devlog.blog.user.service;

import io.devlog.blog.board.entity.Board;
import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.board.repository.BoardRepository;
import io.devlog.blog.board.repository.BoardTagsRepository;
import io.devlog.blog.board.repository.CateRepository;
import io.devlog.blog.config.CustomException;
import io.devlog.blog.config.ResponseCheck;
import io.devlog.blog.config.enums.ExceptionStatus;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.oauth.functions.OAuthHandler;
import io.devlog.blog.oauth.values.GITHUB;
import io.devlog.blog.oauth.values.GOOGLE;
import io.devlog.blog.oauth.values.NAVER;
import io.devlog.blog.pblog.DTO.PblogDTO;
import io.devlog.blog.pblog.Entity.PBlog;
import io.devlog.blog.pblog.repository.PblogRepository;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.security.Jwt.JwtToken;
import io.devlog.blog.team.repository.TBlogRepository;
import io.devlog.blog.user.DTO.OAuthDTO;
import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.DTO.UserInfoDTO;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.entity.UserInfo;
import io.devlog.blog.user.repository.SubscribesRepository;
import io.devlog.blog.user.repository.UserInfoRepository;
import io.devlog.blog.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserInfoRepository userInfoRepository;
    private final HttpServletRequest httpServletRequest;
    private final SubscribesRepository subscribesRepository;
    private final PblogRepository pblogRepository;
    private final CateRepository cateRepository;
    private final BoardRepository boardRepository;
    private final BoardTagsRepository boardTagsRepository;
    private final TBlogRepository tBlogRepository;

    @Autowired
    private final NAVER naver;
    @Autowired
    private final GOOGLE google;
    @Autowired
    private final GITHUB github;


    public UserServiceImpl(final UserRepository userRepository, PasswordEncoder pwEncoder,
                           JwtService jwtService, HttpServletResponse httpServletResponse,
                           UserInfoRepository userInfoRepository, HttpServletRequest httpServletRequest,
                           SubscribesRepository subscribesRepository, PblogRepository pblogRepository,
                           CateRepository cateRepository, BoardRepository boardRepository,
                           BoardTagsRepository boardTagsRepository, TBlogRepository tBlogRepository,
                           NAVER naver, GOOGLE google, GITHUB github
    ) {
        super(User.class);
        this.userRepository = userRepository;
        this.pwEncoder = pwEncoder;
        this.jwtService = jwtService;
        this.httpServletResponse = httpServletResponse;
        this.userInfoRepository = userInfoRepository;
        this.httpServletRequest = httpServletRequest;
        this.subscribesRepository = subscribesRepository;
        this.pblogRepository = pblogRepository;
        this.cateRepository = cateRepository;
        this.boardRepository = boardRepository;
        this.boardTagsRepository = boardTagsRepository;
        this.tBlogRepository = tBlogRepository;
        this.naver = naver;
        this.google = google;
        this.github = github;
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
                return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
            } else {
                return ResponseEntity.ok().body(finds);
            }
        } catch (Exception e) {
            log.error(e);
            throw new CustomException(ExceptionStatus.BAD_REQUEST);
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
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            }
            if (!pwEncoder.matches(user.getPw(), find.get().getUserPw())) {
                log.info("Password not match");
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            } else {
                log.info("Login success");
                log.info("Set cookie");
                setCookie(find);
                return responseLogin(UserDTO.toDTO(find.get()));
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }

    /**
     * Login with OAuth
     *
     * @param oauthDTO OAuthDTO (provider, code, state)
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> loginOauth(OAuthDTO oauthDTO) {
        try {
            OAuthHandler oAuthHandler = new OAuthHandler();
            Optional<User> find;
            switch (oauthDTO.getState()) {
                case "naver" -> {
                    String naverUuid = oAuthHandler.loginNaver(oauthDTO.getCode(), oauthDTO.getState(), naver).getUuid();
                    find = userRepository.findByBenderUuid(naverUuid);
                }
                case "github" -> {
                    String githubUuid = oAuthHandler.loginGithub(oauthDTO.getCode(), oauthDTO.getState(), github).getUuid();
                    find = userRepository.findByBenderUuid(githubUuid);
                }
                case "google" -> {
                    String googleUuid = oAuthHandler.loginGoogle(oauthDTO.getCode(), oauthDTO.getState(), google).getUuid();
                    find = userRepository.findByBenderUuid(googleUuid);
                }
                default -> {
                    return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
                }
            }
            find.ifPresent((user) ->
                    setCookie(Optional.of(user))
            );
            if (find.isPresent()) {
                return responseLogin(UserDTO.toDTO(find.get()));
            } else {
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }

    /**
     * <h1>Auto Login</h1>
     *
     * @param refreshToken refresh token
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> login(String refreshToken) {
        try {
            log.debug("Auto login: {}", refreshToken);
            if (jwtService.validateToken(refreshToken)) {
                long id = jwtService.getClaims(refreshToken).get("id", Long.class);
                boolean isExist = userRepository.existsByUserUuid(id);
                if (!isExist) {
                    log.error("Invalid user");
                    return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
                }
                String accessToken = jwtService.createAccessToken(jwtService.getClaims(refreshToken).get("id", Long.class));
                JwtToken jwtToken = new JwtToken(accessToken, refreshToken);
                httpServletResponse.addHeader("Authorization", jwtToken.getAccessToken());
                if (userRepository.findOneByUserUuid(id).isPresent()) {
                    log.info("<Token auto login> Login success");
                    return responseLogin(UserDTO.toDTO(userRepository.findOneByUserUuid(id).get()));
                } else {
                    log.error("<Token auto login> User not exist");
                    return ResponseEntity.badRequest().body(ExceptionStatus.USER_NOT_FOUND);
                }
            } else {
                log.error("<Token auto login> Token expired");
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<?> responseLogin(UserDTO r) {
        r.setPw(null);
        r.setBenderUuid(null);
        r.setBender(null);
        return ResponseEntity.status(200).body(r);
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
            String accessToken = jwtService.createAccessToken(u.get().getUserUuid());
            JwtToken jwtToken = new JwtToken(accessToken, jwtService.createRefreshToken(u.get().getUserUuid()));
            log.info("Token created");
            httpServletResponse.addHeader("Authorization", jwtToken.getAccessToken());
            Cookie cookie = new Cookie("refreshToken", jwtToken.getRefreshToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 7);
            httpServletResponse.addCookie(cookie);
        }
    }

    /**
     * Logout
     *
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> logout() {
        log.info("User Logout");
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok().body(new ResponseCheck(Status.OK));
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
                return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
            }
            if (userRepository.findOneByUserId(user.getId()).isPresent()) {
                log.error("사용중인 ID : {}", user);
                return ResponseEntity.badRequest().body(ExceptionStatus.CONFLICT);
            } else {
                user.setPw(pwEncoder.encode(user.getPw()));
                User check = userRepository.save(user.toEntity());
                UserInfoDTO info = new UserInfoDTO(null, null, null, null, null);
                UserInfo ui = info.toEntity();
                ui.setUser(check);
                userInfoRepository.save(ui);
                check.setUserInfo(ui);
                PblogDTO pblog = new PblogDTO(check.getName(), null, check.getName());
                PBlog pb = pblog.toEntity();
                pb.setUser(check);
                check.setPbLog(pb);
                userRepository.save(check);
                PBlog p = pblogRepository.save(pb);
                Categories categories = Categories.builder()
                        .cateName("기본 카테고리")
                        .cateIdx(0)
                        .userUuid(check.getUserUuid())
                        .pBlog(p)
                        .tBlog(null)
                        .build();
                cateRepository.save(categories);
                UserDTO returnData = UserDTO.toDTO(check);
                log.info("Created user: {}", returnData);
                return ResponseEntity.ok().body(new ResponseCheck(Status.CREATED));
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }

    /**
     * Update user
     *
     * @param user UserDTO (id, pw, bender, benderUuid, name, mail)
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> update(UserDTO user) {
        try {
            long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
            if (userRepository.findOneByUserUuid(id).isEmpty()) {
                log.error("User not exist");
                return ResponseEntity.badRequest().body(ExceptionStatus.USER_NOT_FOUND);
            } else {
                String pw = pwEncoder.encode(user.getPw());
                user.setPw(pw);
                int e = userRepository.updateUserByUserUuid(id, pw, user.getName(), user.getMail());
                if (e == 0) {
                    log.error("Update failed");
                    return ResponseEntity.badRequest().body(ExceptionStatus.NOT_MODIFIED);
                }
                return ResponseEntity.ok().body(new ResponseCheck(Status.OK));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }

    /***
     * Delete user
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> deleteUser() {
        try {
            long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
            log.info("Deleting user: {}", id);
            Optional<User> user = userRepository.findOneByUserUuid(id);
            if (user.isEmpty()) {
                log.error("Already deleted user");
                return ResponseEntity.badRequest().body(ExceptionStatus.USER_NOT_FOUND);
            } else {
                if (tBlogRepository.findTBlogByUserUuid(id) != null) {
                    return ResponseEntity.badRequest().body("팀 블로그를 먼저 삭제 해 주세요 !");
                }
                Optional<List<Board>> board = boardRepository.findBoardByUserUuid(id);
                if (board.isPresent()) {
                    for (Board b : board.get()) {
                        boardTagsRepository.deleteByBoardUuid(b.getBoardUuid());
                    }
                }
                List<Categories> categories = cateRepository.findByUserUuid(id);
                if (!categories.isEmpty()) {
                    for (Categories c : categories) {
                        boardRepository.deleteBoardsByCateUuid(c.getCateUuid());
                    }
                }
                cateRepository.deleteAllByUserUuid(id);
                pblogRepository.deletePBlogByUserUuid(id);
                userInfoRepository.deleteByUserUuid(id);
                subscribesRepository.deleteAllByUserUuid(id);
                userRepository.deleteByUserUuid(id);
                return ResponseEntity.ok().body(new ResponseCheck(Status.OK));
            }
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }

    /**
     * Check password
     *
     * @param password password
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> passwordCheck(String password) {
        try {
            long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
            if (userRepository.findOneByUserUuid(id).isPresent()) {
                log.info("Checking password");
                log.info("Password: {}", password);
                log.info("User: {}", userRepository.findOneByUserUuid(id).get().getUserPw());
                if (pwEncoder.matches(password, userRepository.findOneByUserUuid(id).get().getUserPw())) {
                    log.info("Password match");
                    return ResponseEntity.ok().body(new ResponseCheck(Status.OK));
                } else {
                    log.error("Password not match");
                    return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
                }
            }
            log.error("<PasswordCheck> User not exist");
            return ResponseEntity.badRequest().body(ExceptionStatus.USER_NOT_FOUND);
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }
}