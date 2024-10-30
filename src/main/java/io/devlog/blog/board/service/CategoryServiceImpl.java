package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.CateDTO;
import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.board.repository.BoardRepository;
import io.devlog.blog.board.repository.BoardTagsRepository;
import io.devlog.blog.board.repository.CateRepository;
import io.devlog.blog.board.repository.TagRepository;
import io.devlog.blog.pblog.Entity.PBlog;
import io.devlog.blog.pblog.repository.PblogRepository;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.team.entity.TBlog;
import io.devlog.blog.team.repository.TBlogRepository;
import io.devlog.blog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    private final PblogRepository pblogRepository;
    private final TBlogRepository tblogRepository;

    public CategoryServiceImpl(BoardRepository boardRepository, CateRepository cateRepository, TagRepository tagRepository, BoardTagsRepository boardTagsRepository, JwtService jwtService, UserRepository userRepository, HttpServletRequest httpServletRequest, PblogRepository pblogRepository, TBlogRepository tblogRepository) {
        this.boardRepository = boardRepository;
        this.cateRepository = cateRepository;
        this.tagRepository = tagRepository;
        this.boardTagsRepository = boardTagsRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.httpServletRequest = httpServletRequest;
        this.pblogRepository = pblogRepository;
        this.tblogRepository = tblogRepository;
    }

    @Override
    public ResponseEntity<?> getCategories() {
        try {
            Long id = jwtService.checkJwt();
            if (id == 0) {
                return ResponseEntity.badRequest().body("Token error");
            }
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
    public ResponseEntity<?> createPCategory(CateDTO cateDTO) {
        try {
            Long id = jwtService.checkJwt();
            if (id == 0) {
                return ResponseEntity.badRequest().body("Token error");
            }
            cateDTO.setUserUuid(id);
            PBlog p = pblogRepository.findPBlogByUserUuid(id);
            Categories categories = Categories.builder()
                    .cateName(cateDTO.getCateName())
                    .cateIdx(cateDTO.getCateIdx())
                    .userUuid(id)
                    .pBlog(p)
                    .tBlog(null)
                    .build();
            cateRepository.save(categories);
            Optional<Categories> category = cateRepository.findByCateNameAndUserUuid(cateDTO.getCateName(), id);
            return ResponseEntity.ok(CateDTO.toDTO(category.get()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("create category error");
        }
    }

    @Override
    public ResponseEntity<?> createTCategory(CateDTO cateDTO) {
        try {
            Long id = jwtService.checkJwt(); //userUuid
            if (id == 0) {
                return ResponseEntity.badRequest().body("Token error");
            }
            cateDTO.setUserUuid(id);
            TBlog t = tblogRepository.findTBlogByUserUuid(id);
            Categories categories = Categories.builder()
                    .cateName(cateDTO.getCateName())
                    .cateIdx(cateDTO.getCateIdx())
                    .userUuid(id)
                    .pBlog(null)
                    .tBlog(t)
                    .build();
            cateRepository.save(categories);
            Optional<Categories> category = cateRepository.findByCateNameAndUserUuid(cateDTO.getCateName(), id);
            return ResponseEntity.ok(CateDTO.toDTO(category.get()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("create category error");
        }
    }

    @Override
    public ResponseEntity<?> updateCateName(CateDTO cateDTO) {
        try {
            Long id = jwtService.checkJwt();
            if (id == 0) {
                return ResponseEntity.badRequest().body("Token error");
            }
            Optional<Categories> existingCategory = cateRepository.findByCateUuid(cateDTO.getCateUuid());
            if (!existingCategory.isEmpty()) {
                Categories category = existingCategory.get();
                if (!category.getCateName().equals(cateDTO.getCateName())) {
                    category.setCateName(cateDTO.getCateName());
                }
                cateRepository.save(category);
                return ResponseEntity.ok("category updated");
            } else {
                return ResponseEntity.badRequest().body("category not found");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("delete category error");
        }
    }

    @Override
    public ResponseEntity<?> updateCategory(List<CateDTO> cateDTOS) {
        try {
            Long id = jwtService.checkJwt();
            if (id == 0) {
                return ResponseEntity.badRequest().body("Token error");
            }
            for (CateDTO cateDTO : cateDTOS) {
                Optional<Categories> existingCategory = cateRepository.findByCateUuid(cateDTO.getCateUuid());
                if (!existingCategory.isEmpty()) {
                    Categories category = existingCategory.get();
                    // cateIdx가 변경됐을 경우 cateIdx 변경해서 저장
                    if (category.getCateIdx() != cateDTO.getCateIdx() || !category.getCateName().equals(cateDTO.getCateName())) {
                        category.setCateIdx(cateDTO.getCateIdx());
                        category.setCateName(cateDTO.getCateName());
                        cateRepository.save(category);
                    }
                } else {
                    cateDTO.setUserUuid(id);
                    cateRepository.save(cateDTO.toEntity());
                }
            }
            return ResponseEntity.ok("category updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("delete category error");
        }
    }

    @Override
    public ResponseEntity<?> deleteCategory(Long cateUuid) {
        try {
            Long id = jwtService.checkJwt();
            if (id == 0) {
                return ResponseEntity.badRequest().body("Token error");
            }
            if (cateRepository.deleteByCategoriId(cateUuid) == 1) {
                return ResponseEntity.status(200).body("category deleted");
            } else {
                return ResponseEntity.badRequest().body("category not found");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body("delete category error");
        }
    }
}
