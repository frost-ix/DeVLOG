package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.CateDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    ResponseEntity<?> getCategories();

    ResponseEntity<?> createCategory(CateDTO cateDTO);

    ResponseEntity<?> updateCateName(CateDTO cateDTO);

    ResponseEntity<?> updateCategory(List<CateDTO> cateDTOS);

    ResponseEntity<?> deleteCategory(Long cateUuid);

}
