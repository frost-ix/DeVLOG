package io.devlog.blog.board.repository;

import io.devlog.blog.board.entity.Board;
import io.devlog.blog.board.entity.BoardTags;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardTagsRepository extends JpaRepository<BoardTags, String> {


    @Modifying
    @Transactional
    void deleteByBoard(Board board);
}
