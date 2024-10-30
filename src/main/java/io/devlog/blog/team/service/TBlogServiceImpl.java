package io.devlog.blog.team.service;

import io.devlog.blog.config.enums.ExceptionStatus;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.team.dto.TBlogDTO;
import io.devlog.blog.team.dto.TBlogRoleDTO;
import io.devlog.blog.team.entity.TBlog;
import io.devlog.blog.team.entity.TBlogRole;
import io.devlog.blog.team.repository.TBlogRepository;
import io.devlog.blog.team.repository.TBlogRoleRepository;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TBlogServiceImpl implements TBlogService {
    private final TBlogRepository tBlogRepository;
    private final JwtService jwtService;
    private final TBlogRoleRepository tBlogRoleRepository;
    private final UserRepository userRepository;

    public TBlogServiceImpl(
            TBlogRepository tBlogRepository,
            JwtService jwtService,
            TBlogRoleRepository tBlogRoleRepository, UserRepository userRepository) {
        this.tBlogRepository = tBlogRepository;
        this.jwtService = jwtService;
        this.tBlogRoleRepository = tBlogRoleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> getTeamBlog() {
        Long id = jwtService.checkJwt();
        if (jwtService.checkJwt() == 0L) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            try {
                Optional<List<TBlog>> tBlog = tBlogRepository.findAllByByUserUuid(id);
                List<TBlogDTO> tBlogDTO = new ArrayList<>();
                tBlog.ifPresent(tBlogs -> tBlogs.forEach(t ->
                        tBlogDTO.add(TBlogDTO.of(
                                t.getTDomain(),
                                t.getTTitle(),
                                t.getTName(),
                                t.getTSubject()
                        ))));
                if (tBlogDTO.isEmpty()) {
                    return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
                } else {
                    return ResponseEntity.ok(tBlogDTO);
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public ResponseEntity<?> getTeamBlogMembers() {
        Long id = jwtService.checkJwt();
        if (jwtService.checkJwt() == 0L) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            try {
                long tUuid = tBlogRepository.findTBlogByUserUuid(id).getTUuid();
                List<TBlogRole> tBlogRole = tBlogRoleRepository.findByTUuid(tUuid);
                if (tBlogRole.isEmpty()) {
                    return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
                } else {
                    List<TBlogRoleDTO> list = new ArrayList<>();
                    tBlogRole.forEach(t -> list.add(TBlogRoleDTO.of(
                                    t.getUserUuid(),
                                    t.getTBlog().getTUuid(),
                                    t.getTeamRole(),
                                    t.getUserIcon(),
                                    t.getMemberDescription()
                            )
                    ));
                    return ResponseEntity.ok(list);
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public ResponseEntity<?> createTeamBlog(TBlogDTO tBlogDTO) {
        Long id = jwtService.checkJwt();
        if (jwtService.checkJwt() == 0L) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            try {
                Optional<User> u = userRepository.findByUserUuid(id);
                if (u.isEmpty()) {
                    return ResponseEntity.badRequest().body(ExceptionStatus.NOT_FOUND);
                }
                TBlog tBlog = TBlog.builder()
                        .tDomain(tBlogDTO.getTDomain())
                        .tTitle(tBlogDTO.getTTitle())
                        .tName(tBlogDTO.getTName())
                        .tSubject(tBlogDTO.getTSubject())
                        .user(u.get())
                        .build();
                if (tBlogRepository.findTBlogByUserUuid(id) == null) {
                    TBlog t = tBlogRepository.save(tBlog);
                    return ResponseEntity.ok().body(Status.OK);
                }
                return ResponseEntity.badRequest().body(ExceptionStatus.CONFLICT);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public ResponseEntity<?> deleteTeamBlog() {
        Long id = jwtService.checkJwt();
        if (jwtService.checkJwt() == 0L) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            try {
                TBlog tBlog = tBlogRepository.findTBlogByUserUuid(id);
                String role = tBlogRoleRepository.findTeamRole(id, tBlog.getTUuid());
                if (role.equals("LEADER")) {
                    tBlogRepository.delete(tBlog);
                    return ResponseEntity.ok().body(Status.OK);
                } else {
                    return ResponseEntity.badRequest().body(ExceptionStatus.FORBIDDEN);
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
            }
        }
    }
}
