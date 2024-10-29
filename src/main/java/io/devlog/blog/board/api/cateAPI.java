package io.devlog.blog.board.api;

import io.devlog.blog.board.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class cateAPI {
    private final CategoryService categoryService;

    public cateAPI(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/cate")
    public ResponseEntity<?> getCategories() {
        return categoryService.getCategories();
    }
    @PostMapping("/cate/create")
    public ResponseEntity<?> createCategory(@RequestBody List<String> cateList) {
        return categoryService.createCategory(cateList);
    }
}
