package io.devlog.blog.board.repository;

import io.devlog.blog.board.entity.Board;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {
    Optional<Board> findOneByBoardUuid(Long id);

    @Query(value = """
                SELECT
                    b.board_uuid AS boardUuid,
                    b.user_name AS userName,
                    b.board_date AS boardDate,
                    b.board_title AS boardTitle,
                    b.board_content AS boardContent,
                    b.board_profilepath AS boardProfilepath,
                    b.visit_count AS visitCount,
                    GROUP_CONCAT(DISTINCT t.tag_name) AS tags
                FROM board b
                LEFT JOIN board_tags bt ON b.board_uuid = bt.board_uuid
                LEFT JOIN tags t ON bt.taguid = t.taguid 
                WHERE b.board_uuid = :boardUuid
                GROUP BY b.board_uuid
            """, nativeQuery = true)
    Map<String, Object> getBoard(@Param("boardUuid") Long boardUuid);

    Board findBoardByBoardUuid(Long boardUuid);

    @Query("SELECT b FROM Board b")
    List<Board> findBoardBy();

    @Query("select b from Board b where b.userUuid = ?1")
    Optional<List<Board>> findBoardByUserUuid(Long userUuid);

    @Query("select b from Board b where b.categories.cateUuid = ?1")
    List<Board> findBoardByUserUuidAndCategoriesCateUuid(Long cateUuid);

    @Query(value = """
            select b.*
            FROM board b
            LEFT JOIN board_tags bt ON b.board_uuid = bt.board_uuid
            LEFT JOIN tags t ON bt.taguid = t.taguid
               WHERE t.tag_name= :tagName
            """, nativeQuery = true)
    List<Board> findBoardByTagsTagName(@Param("tagName") String tagName);

    @Modifying
    @Transactional
    @Query("delete from Board b where b.boardUuid = ?1")
    void deleteById(Long boardUuid);

    Slice<Board> findBoardByCategoriesCateUuid(long cateUuid, Pageable p);

    @Transactional
    @Modifying
    @Query("delete from Board b where b.userUuid = :userUuid")
    void deleteBoardsById(@Param("userUuid") Long userUuid);

    @Transactional
    @Modifying
    @Query("delete from Board b where b.categories.cateUuid = :cateUuid")
    void deleteBoardsByCateUuid(@Param("cateUuid") Long cateUuid);
}