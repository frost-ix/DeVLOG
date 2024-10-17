package io.devlog.blog.board.repository;

import io.devlog.blog.board.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CateRepository extends JpaRepository<Categories, String> {
    Optional<Categories> findByCateName(String cateName);
}