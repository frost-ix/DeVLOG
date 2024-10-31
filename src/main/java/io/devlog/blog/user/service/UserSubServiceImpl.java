package io.devlog.blog.user.service;

import io.devlog.blog.config.CustomException;
import io.devlog.blog.config.enums.ExceptionStatus;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.pblog.DTO.PblogDTO;
import io.devlog.blog.pblog.Entity.PBlog;
import io.devlog.blog.pblog.repository.PblogRepository;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.DTO.SubscribesDTO;
import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.DTO.UserInfoDTO;
import io.devlog.blog.user.entity.Subscribes;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.entity.UserInfo;
import io.devlog.blog.user.repository.SubscribesRepository;
import io.devlog.blog.user.repository.UserInfoRepository;
import io.devlog.blog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class UserSubServiceImpl extends QuerydslRepositorySupport implements UserSubService {
    private final SubscribesRepository sbRepo;
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final HttpServletRequest httpServletRequest;
    private final UserInfoRepository userInfoRepository;
    private final PblogRepository pblogRepository;

    public UserSubServiceImpl(final SubscribesRepository sbRepo, final UserRepository userRepo, JwtService jwtService, HttpServletRequest httpServletRequest, UserInfoRepository userInfoRepository, PblogRepository pblogRepository) {
        super(Subscribes.class);
        this.sbRepo = sbRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;
        this.userInfoRepository = userInfoRepository;
        this.pblogRepository = pblogRepository;
    }

    @Override
    public ResponseEntity<?> getUsersSubCount() {
        long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
        User user = userRepo.findByUserUuid(id).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(ExceptionStatus.NOT_FOUND);
        } else {
            int count = sbRepo.countBySubUserId(user.getUserId());
            return ResponseEntity.ok(count);
        }
    }

    @Override
    public ResponseEntity<?> getUserSub() {
        try {
            long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
            List<Subscribes> finds = sbRepo.findByUserUuid(id);
            if (finds.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                List<UserDTO> subUsers = new ArrayList<>();
                finds.forEach((s) -> {
                    Optional<User> i = userRepo.findByUserId(s.getSubUserId());
                    if (i.isEmpty()) {
                    } else {
                        long iUuid = i.get().getUserUuid();
                        if (iUuid != 0) {
                            UserInfo uI = userInfoRepository.findByUserUuid(iUuid);
                            PBlog pBlog = pblogRepository.findPBlogByUserUuid(i.get().getUserUuid());
                            int count = sbRepo.countBySubUserId(i.get().getUserId());
                            UserDTO e = new UserDTO(
                                    i.get().getUserId(), null, null, null,
                                    i.get().getName(), i.get().getMail(),
                                    UserInfoDTO.toDTO(uI), PblogDTO.fromEntity(pBlog), count,
                                    s.getSubDate());
                            subUsers.add(e);
                        }
                    }
                });
                return ResponseEntity.ok(subUsers);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }

    @Override
    public ResponseEntity<?> addUserSub(SubscribesDTO sbDTO) {
        try {
            long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
            Optional<List<User>> user = userRepo.findAllByUserUuid(id);
            if (user.isEmpty()) {
                return ResponseEntity.badRequest().body(ExceptionStatus.NOT_FOUND);
            }
            Subscribes s = sbRepo.findBySubUser(sbDTO.getSubUserId());
            if (s != null) {
                return ResponseEntity.badRequest().body(ExceptionStatus.CONFLICT);
            } else {
                Optional<User> i = userRepo.findByUserUuid(id);
                if (i.isEmpty()) {
                    return ResponseEntity.badRequest().body(ExceptionStatus.NOT_FOUND);
                }
                Subscribes sb = Subscribes.builder()
                        .user(i.get())
                        .subUserId(sbDTO.getSubUserId())
                        .build();
                sbRepo.save(sb);
                return ResponseEntity.ok(Status.CREATED);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }


    @Override
    public ResponseEntity<?> deleteUserSub(String subUserId) {
        try {
            long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
            if (id == 0) {
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            } else {
                Subscribes s = sbRepo.findBySubUser(subUserId);
                if (s == null) {
                    return ResponseEntity.badRequest().body(ExceptionStatus.NOT_FOUND);
                } else {
                    sbRepo.delete(s);
                    return ResponseEntity.ok(Status.OK);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }

    @Override
    public ResponseEntity<?> deleteUserSubs() {
        try {
            long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
            log.info(id);
            sbRepo.deleteAllByUserUuid(id);
            return ResponseEntity.ok(Status.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }
}
