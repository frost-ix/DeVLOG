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
                    t.getTBanner(),
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
    public ResponseEntity<?> getTeamBlog(String domain) {
        try {
            Optional<TBlog> tBlog = tBlogRepository.findByTDomain(domain);
            if (tBlog.isEmpty()) {
                return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
            } else {
                return ResponseEntity.ok(TBlogDTO.toDTO(tBlog.get()));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getTeamBlogMembers(String tDomain) {
        try {
            List<TBlogRole> tBlogRole = tBlogRoleRepository.findByTDomain(tDomain);
            if (tBlogRole == null) {
                return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
            } else {
                List<String> userNames = new ArrayList<>();
                tBlogRole.forEach(t -> {
                    Optional<User> user = userRepository.findByUserUuid(t.getUserUuid());
                    user.ifPresent(value -> userNames.add(value.getName()));
                });
                List<TBlogRoleDTO> memberList = new ArrayList<>();
                for (int i = 0; i < userNames.size(); i++) {
                    memberList.add(TBlogRoleDTO.of(
                            tBlogRole.get(i).getTeamRole(),
                            tBlogRole.get(i).getUserIcon(),
                            tBlogRole.get(i).getMemberDescription(),
                            userNames.get(i)
                    ));
                }
                return ResponseEntity.ok().body(memberList);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getMyTeamBlog() {
        try {
            Long id = jwtService.checkJwt();
            if (id == 0L) {
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            } else {
                TBlog tBlog = tBlogRepository.findByUserUuid(id);
                if (tBlog == null) {
                    return ResponseEntity.status(204).body(ExceptionStatus.NO_CONTENT);
                } else {
                    TBlogDTO myBlog = TBlogDTO.of(
                            tBlog.getTUuid(),
                            tBlog.getTDomain(),
                            tBlog.getTTitle(),
                            tBlog.getTName(),
                            tBlog.getTBanner(),
                            tBlog.getTSubject(),
                            tBlog.getTInfo()
                    );
                    return ResponseEntity.ok(myBlog);
                }
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
                            .tDomain(tBlogDTO.getTDomain())
                            .tTitle(tBlogDTO.getTTitle())
                            .tName(tBlogDTO.getTName())
                            .tSubject(tBlogDTO.getTSubject())
                            .tInfo(tBlogDTO.getTInfo())
                            .user(u.get())
                            .build();
                    TBlog t = tBlogRepository.save(tBlog);
                    TBlogRoleDTO tBlogRoleDTO = TBlogRoleDTO.of(
                            id, tBlog.getTUuid(), "LEADER",
                            u.get().getUserInfo().getUserIcon(),
                            "팀장 입니다.");
                    TBlogRole tBlogRole = tBlogRoleDTO.toEntity(tBlogRoleDTO, t);
                    tBlogRoleRepository.save(tBlogRole);
                    Categories categories = Categories.builder()
                            .cateName("기본 카테고리")
                            .userUuid(id)
                            .cateIdx(0)
                            .tBlog(tBlog)
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
    public ResponseEntity<?> updateTeamBlogDomain(TBlogDTO tBlogDTO) {
        Long id = jwtService.checkJwt();
        if (jwtService.checkJwt() == 0L) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            try {
                TBlog tBlog = tBlogRepository.findTBlogByUserUuid(id);
                String role = tBlogRoleRepository.findTeamRole(id, tBlog.getTUuid());
                if (role.equals("LEADER")) {
                    tBlog.setTDomain(tBlogDTO.getTDomain());
                    tBlogRepository.updateTBlogDomain(tBlog.getTUuid(), tBlog.getTDomain());
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
    public ResponseEntity<?> updateTeamBlogTitle(TBlogDTO tBlogDTO) {
        Long id = jwtService.checkJwt();
        if (jwtService.checkJwt() == 0L) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            try {
                TBlog tBlog = tBlogRepository.findTBlogByUserUuid(id);
                String role = tBlogRoleRepository.findTeamRole(id, tBlog.getTUuid());
                if (role.equals("LEADER")) {
                    tBlog.setTTitle(tBlogDTO.getTTitle());
                    tBlogRepository.updateTBlogTitle(tBlog.getTUuid(), tBlog.getTTitle());
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
    public ResponseEntity<?> updateTeamBlogBanner(TBlogDTO tBlogDTO) {
        Long id = jwtService.checkJwt();
        if (jwtService.checkJwt() == 0L) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            try {
                TBlog tBlog = tBlogRepository.findTBlogByUserUuid(id);
                String role = tBlogRoleRepository.findTeamRole(id, tBlog.getTUuid());
                if (role.equals("LEADER")) {
                    tBlogRepository.updateTBlogBanner(tBlog.getTUuid(), tBlogDTO.getTBanner());
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
    public ResponseEntity<?> updateTeamBlogInfo(TBlogDTO tBlogDTO) {
        Long id = jwtService.checkJwt();
        if (jwtService.checkJwt() == 0L) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            try {
                TBlog tBlog = tBlogRepository.findTBlogByUserUuid(id);
                String role = tBlogRoleRepository.findTeamRole(id, tBlog.getTUuid());
                if (role.equals("LEADER")) {
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
    public ResponseEntity<?> updateTeamBlogName(TBlogDTO tBlogDTO) {
        Long id = jwtService.checkJwt();
        if (jwtService.checkJwt() == 0L) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            try {
                TBlog tBlog = tBlogRepository.findTBlogByUserUuid(id);
                String role = tBlogRoleRepository.findTeamRole(id, tBlog.getTUuid());
                if (role.equals("LEADER")) {
                    tBlog.setTName(tBlogDTO.getTName());
                    tBlogRepository.updateTBlogName(tBlog.getTUuid(), tBlog.getTName());
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
