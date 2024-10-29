package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.CateDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    ResponseEntity<?> getCategories();

    ResponseEntity<?> createCategory(List<CateDTO> cateDTOS);

    ResponseEntity<?> deleteCategory(Long cateUuid);

}
