package io.devlog.blog.board.repository;

import io.devlog.blog.board.entity.Board;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {
    Optional<Board> findOneByBoardUuid(Long id);

    @Query("SELECT b FROM Board b")
    List<Board> findBoardBy();

    @Query("select b from Board b where b.userUuid = ?1")
    Optional<List<Board>> findBoardByUserUuid(Long userUuid);

//    Slice<Board> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from Board b where b.boardUuid = ?1")
    void deleteById(Long boardUuid);
}