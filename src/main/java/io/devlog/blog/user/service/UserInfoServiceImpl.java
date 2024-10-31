package io.devlog.blog.user.service;

import io.devlog.blog.config.CustomException;
import io.devlog.blog.config.enums.ExceptionStatus;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.DTO.UserInfoDTO;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.entity.UserInfo;
import io.devlog.blog.user.repository.UserInfoRepository;
import io.devlog.blog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class UserInfoServiceImpl extends QuerydslRepositorySupport implements UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final HttpServletRequest httpServletRequest;

    public UserInfoServiceImpl(final UserInfoRepository userInfoRepository, UserRepository userRepository, JwtService jwtService, HttpServletRequest httpServletRequest) {
        super(UserInfo.class);
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public ResponseEntity<?> getUserInfo() {
        long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
        if (id != 0) {
            return ResponseEntity.ok(userInfoRepository.findByUserUuid(id));
        } else {
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.UNAUTHORIZED));
        }
    }

    @Override
    public ResponseEntity<?> getPBlogUserInfo(String domain) {
        try {
            UserInfo userInfo = userInfoRepository.findByPDomain(domain);
            if (userInfo == null) {
                return ResponseEntity.badRequest().body(ExceptionStatus.NOT_FOUND);
            } else {
                return ResponseEntity.ok().body(userInfo);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }


    @Override
    public ResponseEntity<?> updateUserInfo(UserInfoDTO info) {
        try {
            long userUuid = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
            if (info == null || userUuid == 0) {
                log.error("Cannot find data");
                return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
            }
            UserInfo u = userInfoRepository.findByUserUuid(userUuid);
            if (u == null) {
                Optional<User> user = userRepository.findByUserUuid(userUuid);
                if (user.isEmpty()) {
                    log.error("Cannot find user");
                    return ResponseEntity.badRequest().body(ExceptionStatus.USER_NOT_FOUND);
                }
                UserInfo ui = info.toEntity();
                ui.setUser(user.get());
                userInfoRepository.save(ui);
                ui.getUser().setUserUuid(userUuid);
                return ResponseEntity.ok().body(Status.OK);
            } else {
                int c = userInfoRepository.updateInformation(
                        userUuid,
                        info.getUserIcon(), info.getUserSummary(),
                        info.getUserGit(), info.getUserX(), info.getUserInsta()
                );
                if (c == 0) {
                    log.error("Cannot update user information");
                    return ResponseEntity.badRequest().body(ExceptionStatus.NOT_MODIFIED);
                } else {
                    return ResponseEntity.ok().body(Status.OK);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }
}
