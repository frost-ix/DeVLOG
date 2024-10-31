package io.devlog.blog.pblog.repository;

import io.devlog.blog.pblog.Entity.PBlog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PblogRepository extends JpaRepository<PBlog, Long> {
    @Query("select p from PBlog p where p.user.userUuid = :userUuid")
    PBlog findPBlogByUserUuid(@Param("userUuid") long userUuid);

    @Query("select p.user.userUuid from PBlog p where p.pDomain = :pDomain")
    Long getPBlogCategories(String pDomain);

    @Transactional
    @Modifying
    @Query("update PBlog p set p.pBanner = :pBanner, p.pDomain = :pDomain" +
            ", p.pName = :pName " +
            "where p.user.userUuid = :userUuid")
    int updatePBlog(@Param("userUuid") long userUuid,
                    @Param("pBanner") String pBanner,
                    @Param("pDomain") String pDomain,
                    @Param("pName") String pName);

    @Query("select p.pDomain from PBlog p where p.user.userUuid = :userUuid")
    String findbyPDomain(Long userUuid);

    @Transactional
    @Modifying
    @Query("delete from PBlog p where p.user.userUuid = :userUuid")
    void deletePBlogByUserUuid(@Param("userUuid") long userUuid);
}
