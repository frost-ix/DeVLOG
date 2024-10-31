package io.devlog.blog.team.repository;

import io.devlog.blog.team.entity.TBlogRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TBlogRoleRepository extends JpaRepository<TBlogRole, String> {
    @Query("select tbr.teamRole from TBlogRole tbr where tbr.userUuid = :userUuid and tbr.tBlog.tUuid = :tUuid")
    String findRoleByUserUuid(long userUuid, long tUuid);

    @Query("select tbr from TBlogRole tbr where tbr.userUuid = :userUuid")
    TBlogRole findByUserUuid(@Param("userUuid") long userUuid);

    @Query("select tbr from TBlogRole tbr where tbr.tBlog.tDomain = :tDomain")
    List<TBlogRole> findByTDomain(@Param("tDomain") String tDomain);

    @Query("select tbr from TBlogRole  tbr where tbr.userUuid = :userUuid")
    List<TBlogRole> findAllByUserUuid(@Param("userUuid") long userUuid);

    @Query("select tbr from TBlogRole tbr where tbr.tBlog.tUuid = :tUuid")
    List<TBlogRole> findByTUuid(@Param("tUuid") long tUuid);


    @Query("select tbr.teamRole from TBlogRole tbr where tbr.userUuid = :userUuid and tbr.tBlog.tUuid = :tUuid")
    String findTeamRole(@Param("userUuid") long userUuid, @Param("tUuid") long tUuid);

    @Query("select count(tbr) > 0 from TBlogRole tbr where tbr.tBlog.tUuid = :tUuid")
    int countMember(@Param("tUuid") long tUuid);

    boolean existsTBlogRoleByUserUuid(long userUuid);
}
