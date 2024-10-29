package io.devlog.blog.board.repository;

import io.devlog.blog.board.entity.Board;
import io.devlog.blog.board.entity.BoardTags;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardTagsRepository extends JpaRepository<BoardTags, String> {
    @Modifying
    @Transactional
    void deleteByBoard(Board board);

    @Modifying
    @Transactional
    @Query("delete from BoardTags bt where bt.board.boardUuid = :boardUuid")
    void deleteByBoardUuid(@Param("boardUuid") Long boardUuid);
}
