package io.devlog.blog.board.repository;

import io.devlog.blog.board.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository <Tags, String> {
    Optional <Tags> findByTagName(String tagName);
    List<Tags> findByTagNameIn(List<String> tagNames);
}
