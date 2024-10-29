package io.devlog.blog.team.repository;

import io.devlog.blog.team.entity.TBlog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TBlogRepository extends JpaRepository<TBlog, String> {
    @Modifying
    @Transactional
    @Query("delete from TBlog tb where tb.tUuid = ?1")
    void deleteByTUuid(Long tUuid);

    @Modifying
    @Transactional
    @Query("delete from TBlog tb where tb.tUuid = :tUuid")
    void deleteByTeamUuid(@Param("tUuid") Long tUuid);
}
