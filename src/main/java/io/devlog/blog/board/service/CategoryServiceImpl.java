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
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
            List<String> cateNames = cateRepository.findByUserCateName(id);
            return ResponseEntity.ok(cateNames);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("get categories error");
        }
    }

    //{"","","",""}
    @Override
    public ResponseEntity<?> createCategory(List<String> cateName) {
        try {
            Long id = checkJWT();
            AtomicInteger index = new AtomicInteger(0);
            for (String name : cateName) {
                int i = index.getAndIncrement();
                //cateName이 중복인데 cateIdx가 다른 경우 바뀐 idx를 저장
                if (cateRepository.findByCateName(name).isPresent()) {
                    if (cateRepository.findByIdx(name) != i) {
                        cateRepository.UpdateCateIdx(name, i);
                    }
                } else {
                    CateDTO cateDTO = CateDTO.builder()
                            .cateName(name)
                            .userUuid(id)
                            .cateIdx(i)
                            .build();
                    Categories categories = cateDTO.toEntity();
                    cateRepository.save(categories);
                }
            }
            return ResponseEntity.ok("category created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("create category error");
        }
    }
}
