package io.devlog.blog.board.api;

import io.devlog.blog.board.DTO.CateDTO;
import io.devlog.blog.board.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/cate")
public class cateAPI {
    private final CategoryService categoryService;

    public cateAPI(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<?> getCategories() {
        return categoryService.getCategories();
    }

    @PostMapping("")
    public ResponseEntity<?> createCategory(@RequestBody CateDTO cateDTO) {
        log.info("Create category : {}", cateDTO);
        return categoryService.createCategory(cateDTO);
    }

    @PatchMapping("/name")
    public ResponseEntity<?> updateCateName(@RequestBody CateDTO cateDTO) {
        return categoryService.updateCateName(cateDTO);
    }

    @PatchMapping("")
    public ResponseEntity<?> updateCategory(@RequestBody List<CateDTO> cateDTO) {
        return categoryService.updateCategory(cateDTO);
    }

    @DeleteMapping("/{cateUuid}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long cateUuid) {
        return categoryService.deleteCategory(cateUuid);
    }
}
