package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.CateDTO;
import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.board.repository.BoardRepository;
import io.devlog.blog.board.repository.BoardTagsRepository;
import io.devlog.blog.board.repository.CateRepository;
import io.devlog.blog.board.repository.TagRepository;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Log4j2
@Service
public class CategoryServiceImpl implements CategoryService {
    private final BoardRepository boardRepository;
    private final CateRepository cateRepository;
    private final TagRepository tagRepository;
    private final BoardTagsRepository boardTagsRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final HttpServletRequest httpServletRequest;

    public CategoryServiceImpl(BoardRepository boardRepository, CateRepository cateRepository, TagRepository tagRepository, BoardTagsRepository boardTagsRepository, JwtService jwtService, UserRepository userRepository, HttpServletRequest httpServletRequest) {
        this.boardRepository = boardRepository;
        this.cateRepository = cateRepository;
        this.tagRepository = tagRepository;
        this.boardTagsRepository = boardTagsRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.httpServletRequest = httpServletRequest;
    }

    public Long checkJWT() {
        try {
            String accessToken = httpServletRequest.getHeader("Authorization");
            Long id = jwtService.getClaims(accessToken).get("id", Long.class);
            if (accessToken == null || id == 0 || !jwtService.validateToken(accessToken)) {
                return 0L;
            } else {
                // 2. accessToken이 있으나 expireDate가 지났으나 refreshToken이 만료 전 이면 accessToken 재발급 후 진행
                if (userRepository.existsByUserUuid(id)) {
                    return id;

                } else {
                    return 0L;
                }
            }
        } catch (Exception e) {
            return 0L;
        }

    }

    @Override
    public ResponseEntity<?> getCategories() {
        try {
            Long id = checkJWT();
            List<Categories> categories = cateRepository.findByUserCateName(id);
            List<CateDTO> cateDTOS = categories.stream()
                    .map(CateDTO::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.status(200).body(cateDTOS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("get categories error");
        }
    }

    //{"","","",""}
    @Override
    public ResponseEntity<?> createCategory(List<CateDTO> cateDTOS) {
        try {
            Long id = checkJWT();
            AtomicInteger index = new AtomicInteger(0);
            for (CateDTO cateDTO : cateDTOS) {
                int i = index.getAndIncrement();
                Optional<Categories> existingCategory = cateRepository.findByCateUuid(cateDTO.getCateUuid());
                if (existingCategory.isPresent()) {
                    Categories category = existingCategory.get();
                    if (!category.getCateName().equals(cateDTO.getCateName())) {
                        category.setCateName(cateDTO.getCateName());
                        cateRepository.save(category);
                    } else if (category.getCateIdx() != i) {
                        category.setCateIdx(i);
                        cateRepository.save(category);
                    }
                } else {
                    cateDTO.setUserUuid(id);
                    cateDTO.setCateIdx(i);
                    Categories categories = cateDTO.toEntity();
                    cateRepository.save(categories);
                }
            }
            return ResponseEntity.ok("category created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("create category error");
        }
    }

    @Override
    public ResponseEntity<?> deleteCategory(Long cateUuid) {
        try {
            if (cateRepository.deleteByCategoriId(cateUuid) == 1) {
                return ResponseEntity.status(200).body("category deleted");
            } else {
                return ResponseEntity.badRequest().body("category not found");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("delete category error");
        }
    }
}
