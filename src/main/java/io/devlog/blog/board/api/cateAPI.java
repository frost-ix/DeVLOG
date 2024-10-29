package io.devlog.blog.board.api;

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

    @PostMapping("/cate/create")
    public ResponseEntity<?> createCategory(@RequestBody List<String> cateList) {
        return categoryService.createCategory(cateList);
    }
}
