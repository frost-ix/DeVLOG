package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.CateDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    ResponseEntity<?> getPCategories();

    ResponseEntity<?> getTCategories();

    ResponseEntity<?> createPCategory(CateDTO cateDTO);


    ResponseEntity<?> createTCategory(CateDTO cateDTO);

    ResponseEntity<?> updatePCateName(CateDTO cateDTO);

    ResponseEntity<?> updatePCategory(List<CateDTO> cateDTOS);

    ResponseEntity<?> updateTCateName(CateDTO cateDTO);

    ResponseEntity<?> updateTCategory(List<CateDTO> cateDTOS);

    ResponseEntity<?> deleteCategory(Long cateUuid);

}
