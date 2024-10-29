package io.devlog.blog.team.service;

import io.devlog.blog.config.enums.ExceptionStatus;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.team.dto.TBlogDTO;
import io.devlog.blog.team.entity.TBlog;
import io.devlog.blog.team.entity.TBlogRole;
import io.devlog.blog.team.repository.TBlogRepository;
import io.devlog.blog.team.repository.TBlogRoleRepository;
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

    public TBlogServiceImpl(
            TBlogRepository tBlogRepository,
            JwtService jwtService,
            TBlogRoleRepository tBlogRoleRepository) {
        this.tBlogRepository = tBlogRepository;
        this.jwtService = jwtService;
        this.tBlogRoleRepository = tBlogRoleRepository;
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
                        tBlogDTO.add(TBlogDTO.of(t.getUser().getUserUuid()))));
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
                List<TBlogDTO> tBlogDTO = new ArrayList<>();
                tBlogRole.forEach(t -> tBlogDTO.add(TBlogDTO.of(t.getUserUuid())));
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
}
