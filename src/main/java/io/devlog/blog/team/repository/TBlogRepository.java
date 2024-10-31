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
    @Query("select tb from TBlog tb where tb.tUuid = :tUuid")
    Optional<TBlog> findByTUuid(@Param("tUuid") long tUuid);

    @Query("select tb from TBlog tb where tb.tDomain = :tDomain")
    Optional<TBlog> findByTDomain(@Param("tDomain") String tDomain);

    @Query("select t.tUuid from TBlog t where t.tDomain = :tDomain")
    Long getTBlogCategory(@Param("tDomain") String tDomain);

    @Query("select tb from TBlog tb where tb.user.userUuid = :userUuid or tb.tBlogRole.userUuid = :userUuid")
    List<TBlog> findTBlogsByTUuid(@Param("userUuid") long userUuid);

    @Query("select tb from TBlog tb where tb.user.userUuid = :userUuid")
    TBlog findTBlogByUserUuid(@Param("userUuid") long userUuid);

    @Query("select tb.tDomain from TBlog tb where tb.user.userUuid = :userUuid")
    String findbyTDomain(@Param("userUuid") long userUuid);

    @Query("select count(tb) from TBlog tb where tb.user.userUuid = :userUuid")
    int countByUserUuid(@Param("userUuid") long userUuid);

    @Transactional
    @Modifying
    @Query("update TBlog tb set tb.tTitle = :tTitle, tb.tSubject = :tSubject, tb.tName = :tName where tb.user.userUuid = :userUuid")
    int updateTBlog(@Param("userUuid") long userUuid,
                    @Param("tTitle") String tTitle,
                    @Param("tSubject") String tSubject,
                    @Param("tName") String tName);

    @Transactional
    @Modifying
    @Query("update TBlog tb set tb.tDomain = :tDomain where tb.user.userUuid = :userUuid")
    int updateTBlogDomain(@Param("userUuid") long userUuid,
                          @Param("tDomain") String tDomain);

    @Transactional
    @Modifying
    @Query("update TBlog tb set tb.tBanner = :tBanner where tb.user.userUuid = :userUuid")
    int updateTBlogBanner(@Param("userUuid") long userUuid,
                          @Param("tBanner") String tBanner);

    @Transactional
    @Modifying
    @Query("update TBlog tb set tb.tTitle = :tTitle where tb.user.userUuid = :userUuid")
    int updateTBlogTitle(@Param("userUuid") long userUuid,
                         @Param("tTitle") String tTitle);

    @Transactional
    @Modifying
    @Query("update TBlog tb set tb.tName = :tName where tb.user.userUuid = :userUuid")
    int updateTBlogName(@Param("userUuid") long userUuid,
                        @Param("tName") String tName);

    @Transactional
    @Modifying
    @Query("update TBlog tb set tb.tSubject = :tSubject where tb.user.userUuid = :userUuid")
    int updateTBlogSubject(@Param("userUuid") long userUuid,
                           @Param("tSubject") String tSubject);

    @Transactional
    @Modifying
    @Query("update TBlog tb set tb.tInfo = :tInfo where tb.user.userUuid = :userUuid")
    int updateTBlogInfo(@Param("userUuid") long userUuid,
                        @Param("tInfo") String tInfo);

    @Transactional
    @Modifying
    @Query("delete from TBlog tb where tb.user.userUuid = :userUuid")
    void deleteTBlogByUserUuid(@Param("userUuid") long userUuid);
}
