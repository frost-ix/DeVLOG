package io.devlog.blog.board.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    ResponseEntity<?> getCategories();
    ResponseEntity<?> createCategory(List<String> cateName);


}
