package io.devlog.blog.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.devlog.blog.config.enums.ExceptionStatus;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.DTO.SubscribesDTO;
import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.entity.Subscribes;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.repository.SubscribesRepository;
import io.devlog.blog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class UserSubServiceImpl extends QuerydslRepositorySupport implements UserSubService {
    private final SubscribesRepository sbRepo;
    private final UserRepository userRepo;
    private final JPAQueryFactory jpaqf;
    private final JwtService jwtService;
    private final HttpServletRequest httpServletRequest;

    public UserSubServiceImpl(final SubscribesRepository sbRepo, final UserRepository userRepo, final JPAQueryFactory jpaqf, JwtService jwtService, HttpServletRequest httpServletRequest) {
        super(Subscribes.class);
        this.sbRepo = sbRepo;
        this.userRepo = userRepo;
        this.jpaqf = jpaqf;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public ResponseEntity<?> getUsersSub() {
        try {
            return ResponseEntity.ok(sbRepo.findAll());
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().body(e);
        }
    }

    @Override
    public ResponseEntity<?> getUsersSubCount() {
        long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
        long count = sbRepo.findByUserUuid(id).size();
        if (count == 0) {
            return ResponseEntity.noContent().build();
        } else {
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
                HashMap<Long, UserDTO> subUsers = new HashMap<>();
                finds.forEach((s) -> {
                    Optional<User> i = userRepo.findByUserUuid(s.getSubUser());
                    if (i.isPresent()) {
                        UserDTO e = UserDTO.toDTO(i.get());
                        e.setPw(null);
                        e.setMail(null);
                        e.setBender(null);
                        e.setBenderUuid(null);
                        subUsers.put(i.get().getUserUuId(), e);
                    }
                });
                return ResponseEntity.ok(subUsers);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> addUserSub(SubscribesDTO sbDTO) {
        try {
            long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
            if (id == sbDTO.getSubUser()) {
                return ResponseEntity.badRequest().body(ExceptionStatus.CONFLICT);
            }
            if (sbRepo.existsBySubUser(sbDTO.getSubUser())) {
                return ResponseEntity.badRequest().body(ExceptionStatus.CONFLICT);
            } else {
                sbDTO.setUserUuid(id);
                sbRepo.save(sbDTO.toEntity());
                return ResponseEntity.ok(Status.CREATED);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<?> deleteUserSub(SubscribesDTO sbDTO) {
        try {
            long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
            sbDTO.setUserUuid(id);
            if (!sbRepo.existsBySubUser(sbDTO.getSubUser())) {
                return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
            } else {
                sbRepo.deleteBySubUser(sbDTO.getSubUser());
                return ResponseEntity.ok(Status.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
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
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }
}
