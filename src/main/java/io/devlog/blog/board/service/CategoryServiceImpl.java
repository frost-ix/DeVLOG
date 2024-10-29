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

    @Override
    public ResponseEntity<?> getCategories() {
        try {
            List<Categories> categories = cateRepository.findByUserCateName(jwtService.checkJwt());
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
    public ResponseEntity<?> createCategory(CateDTO cateDTO) {
        try {
            cateDTO.setUserUuid(jwtService.checkJwt());
            cateRepository.save(cateDTO.toEntity());
            Optional<Categories> category = cateRepository.findByCateName(cateDTO.getCateName());
            if (category.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok().body(CateDTO.toDTO(category.get()));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("create category error");
        }
    }

    @Override
    public ResponseEntity<?> updateCateName(CateDTO cateDTO) {
        try {
            Optional<Categories> existingCategory = cateRepository.findByCateUuid(cateDTO.getCateUuid());
            if (existingCategory.isPresent()) {
                Categories category = existingCategory.get();
                if (!category.getCateName().equals(cateDTO.getCateName())) {
                    category.setCateName(cateDTO.getCateName());
                    cateRepository.save(category);
                }
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
            for (CateDTO cateDTO : cateDTOS) {
                Optional<Categories> existingCategory = cateRepository.findByCateUuid(cateDTO.getCateUuid());
                if (!existingCategory.isEmpty()) {
                    Categories category = existingCategory.get();
                    // cateIdx가 변경됐을 경우 cateIdx 변경해서 저장
                    if (category.getCateIdx() != cateDTO.getCateIdx()) {
                        category.setCateIdx(cateDTO.getCateIdx());
                        cateRepository.save(category);
                    }
                } else {
                    return ResponseEntity.badRequest().body("category not found");
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
