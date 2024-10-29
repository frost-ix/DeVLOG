package io.devlog.blog.team.repository;

import io.devlog.blog.team.entity.TBlog;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
