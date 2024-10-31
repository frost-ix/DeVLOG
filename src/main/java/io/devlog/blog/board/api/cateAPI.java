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

    @GetMapping("/pBlog")
    public ResponseEntity<?> getCategories() {
        return categoryService.getPCategories();
    }

    @GetMapping("/pBlog/{pDomain}")
    public ResponseEntity<?> getPCategories(@PathVariable String pDomain) {
        return categoryService.getBlogCategories(pDomain, "p");
    }

    @GetMapping("/tBlog/{tDomain}")
    public ResponseEntity<?> getTCategories(@PathVariable String tDomain) {
        return categoryService.getBlogCategories(tDomain, "t");
    }

    @PostMapping("/pBlog")
    public ResponseEntity<?> createCategory(@RequestBody CateDTO cateDTO) {
        log.info("Create category : {}", cateDTO);
        return categoryService.createPCategory(cateDTO);
    }

    @PatchMapping("/pBlog/name")
    public ResponseEntity<?> updateCateName(@RequestBody CateDTO cateDTO) {
        return categoryService.updatePCateName(cateDTO);
    }

    @PatchMapping("/pBlog")
    public ResponseEntity<?> updateCategory(@RequestBody List<CateDTO> cateDTO) {
        return categoryService.updatePCategory(cateDTO);
    }

    @DeleteMapping("/pBlog/{cateUuid}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long cateUuid) {
        return categoryService.deleteCategory(cateUuid);
    }


    @GetMapping("/tBlog")
    public ResponseEntity<?> getTCategories() {
        return categoryService.getTCategories();
    }

    @PostMapping("/tBlog")
    public ResponseEntity<?> createTCategory(@RequestBody CateDTO cateDTO) {
        log.info("Create category : {}", cateDTO);
        return categoryService.createTCategory(cateDTO);
    }

    @PatchMapping("/tBlog/name")
    public ResponseEntity<?> updateTCateName(@RequestBody CateDTO cateDTO) {
        return categoryService.updateTCateName(cateDTO);
    }

    @PatchMapping("/tBlog")
    public ResponseEntity<?> updateTCategory(@RequestBody List<CateDTO> cateDTO) {
        return categoryService.updateTCategory(cateDTO);
    }

}
