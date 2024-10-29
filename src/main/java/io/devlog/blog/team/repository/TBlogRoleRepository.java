package io.devlog.blog.team.repository;

import io.devlog.blog.team.entity.TBlogRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TBlogRoleRepository extends JpaRepository<TBlogRole, String> {
    @Query("select tbr from TBlogRole tbr where tbr.userUuid = :userUuid")
    TBlogRole findByUserUuid(@Param("userUuid") long userUuid);

    @Query("select tbr from TBlogRole tbr where tbr.tBlog.tUuid = :tUuid")
    List<TBlogRole> findByTUuid(@Param("tUuid") long tUuid);

    boolean existsTBlogRoleByUserUuid(long userUuid);
}
