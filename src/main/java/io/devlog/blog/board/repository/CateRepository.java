package io.devlog.blog.board.repository;

import io.devlog.blog.board.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CateRepository extends JpaRepository<Categories, String> {
}