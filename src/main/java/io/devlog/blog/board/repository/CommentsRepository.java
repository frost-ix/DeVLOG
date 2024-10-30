package io.devlog.blog.board.repository;

import io.devlog.blog.board.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByBoard_BoardUuid(Long boardUuid);

    @Query("select c.userUuid from Comments c where c.commentUuid = ?1")
    Long findByUserUuid(Long commentUuid);

    @Query("select c from Comments c where c.userUuid = ?1 and c.board.boardUuid = ?2 and c.commentUuid = ?3")
    Comments findByUserUuidAndBoard_BoardUuid(Long userUuid, Long boardUuid, Long commentUuid);

    @Query("select count(c) from Comments c where c.board.boardUuid = :boardUuid")
    int countByBoard_BoardUuid(Long boardUuid);
}
