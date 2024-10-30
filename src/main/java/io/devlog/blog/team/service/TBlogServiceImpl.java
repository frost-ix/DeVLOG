package io.devlog.blog.team.service;

import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.board.repository.CateRepository;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class TBlogServiceImpl implements TBlogService {
    private final TBlogRepository tBlogRepository;
    private final JwtService jwtService;
    private final TBlogRoleRepository tBlogRoleRepository;
    private final UserRepository userRepository;
    private final CateRepository cateRepository;

    public TBlogServiceImpl(
            TBlogRepository tBlogRepository,
            JwtService jwtService,
            TBlogRoleRepository tBlogRoleRepository, UserRepository userRepository, CateRepository cateRepository) {
        this.tBlogRepository = tBlogRepository;
        this.jwtService = jwtService;
        this.tBlogRoleRepository = tBlogRoleRepository;
        this.userRepository = userRepository;
        this.cateRepository = cateRepository;
    }

    @Override
    public ResponseEntity<?> getTeamBlog() {
        try {
            List<TBlog> tBlog = tBlogRepository.findAll();
            List<TBlogDTO> tBlogDTO = new ArrayList<>();
            tBlog.forEach(t -> tBlogDTO.add(TBlogDTO.of(
                    t.getTUuid(),
                    t.getTDomain(),
                    t.getTTitle(),
                    t.getTName(),
                    t.getTSubject(),
                    t.getTInfo()
            )));
            if (tBlogDTO.isEmpty()) {
                return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
            } else {
                return ResponseEntity.ok(tBlogDTO);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
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
    public ResponseEntity<?> getMyTeamBlog() {
        try {
            Long id = jwtService.checkJwt();
            if (id == 0L) {
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            } else {
                Optional<List<TBlog>> tBlogs = tBlogRepository.findAllByByUserUuid(id);
                if (tBlogs.isEmpty()) {
                    return ResponseEntity.badRequest().body(ExceptionStatus.NOT_FOUND);
                }
                List<TBlogDTO> tBlogDTO = new ArrayList<>();
                tBlogs.get().forEach(t -> tBlogDTO.add(TBlogDTO.of(
                        t.getTUuid(),
                        t.getTDomain(),
                        t.getTTitle(),
                        t.getTName(),
                        t.getTSubject(),
                        t.getTInfo()
                )));
                return ResponseEntity.ok(tBlogDTO);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
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
                } else if (tBlogRepository.findTBlogByUserUuid(id) == null) {
                    log.info("tBlogDTO: {}", tBlogDTO);
                    TBlog tBlog = TBlog.builder()
                            .tDomain("#" + tBlogDTO.getTDomain())
                            .tTitle(tBlogDTO.getTTitle())
                            .tName(tBlogDTO.getTName())
                            .tSubject(tBlogDTO.getTSubject())
                            .user(u.get())
                            .build();
                    log.info("tBlog: {}", tBlog);
                    TBlog t = tBlogRepository.save(tBlog);
                    TBlogRoleDTO tBlogRoleDTO = TBlogRoleDTO.of(id, t.getTUuid(), "LEADER", u.get().getUserInfo().getUserIcon(), "팀장 입니다.");
                    TBlogRole tBlogRole = tBlogRoleDTO.toEntity(t);
                    tBlogRoleRepository.save(tBlogRole);
                    Categories categories = Categories.builder()
                            .cateName("기본 카테고리")
                            .userUuid(id)
                            .cateIdx(0)
                            .tBlog(t)
                            .build();
                    cateRepository.save(categories);
                    return ResponseEntity.ok().body(Status.CREATED);
                } else {
                    return ResponseEntity.badRequest().body(ExceptionStatus.CONFLICT);
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public ResponseEntity<?> updateTeamBlog(TBlogDTO tBlogDTO) {
        Long id = jwtService.checkJwt();
        if (jwtService.checkJwt() == 0L) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            try {
                TBlog tBlog = tBlogRepository.findTBlogByUserUuid(id);
                String role = tBlogRoleRepository.findTeamRole(id, tBlog.getTUuid());
                if (role.equals("LEADER")) {
                    tBlog.setTDomain(tBlogDTO.getTDomain());
                    tBlog.setTTitle(tBlogDTO.getTTitle());
                    tBlog.setTName(tBlogDTO.getTName());
                    tBlog.setTSubject(tBlogDTO.getTSubject());
                    tBlog.setTInfo(tBlogDTO.getTInfo());
                    tBlogRepository.save(tBlog);
                    return ResponseEntity.ok().body(Status.OK);
                } else {
                    return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
                }
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
                    return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
            }
        }
    }
}
