package io.devlog.blog.team.repository;

import io.devlog.blog.team.entity.TBlog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TBlogRepository extends JpaRepository<TBlog, String> {
    @Query("select tb from TBlog tb where tb.user.userUuid = :userUuid")
    Optional<List<TBlog>> findAllByByUserUuid(@Param("userUuid") long userUuid);

    @Query("select tb from TBlog tb where tb.user.userUuid = :userUuid")
    TBlog findTBlogByUserUuid(@Param("userUuid") long userUuid);


    @Transactional
    @Modifying
    @Query("update TBlog tb set tb.tTitle = :tTitle, tb.tSubject = :tSubject, tb.tName = :tName where tb.user.userUuid = :userUuid")
    int updateTBlog(@Param("userUuid") long userUuid,
                    @Param("tTitle") String tTitle,
                    @Param("tSubject") String tSubject,
                    @Param("tName") String tName);

    @Transactional
    @Modifying
    @Query("delete from TBlog tb where tb.user.userUuid = :userUuid")
    void deleteTBlogByUserUuid(@Param("userUuid") long userUuid);
}
