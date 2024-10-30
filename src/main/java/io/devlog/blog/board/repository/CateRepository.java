package io.devlog.blog.board.repository;

import io.devlog.blog.board.entity.Categories;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CateRepository extends JpaRepository<Categories, String> {

    @Query("select c from Categories c where c.userUuid = :userUuid")
    List<Categories> findByUserUuid(Long userUuid);

    Optional<Categories> findByCateUuid(Long cateUuid);

    Optional<Categories> findByCateName(String cateName);

    Optional<Categories> findByCateNameAndUserUuid(String cateName, Long id);

    @Query("select c.cateIdx from Categories c where c.cateName = :name")
    int findByCateIdx(String name);

    @Modifying
    @Query("update Categories c set c.cateIdx = :cateIdx where c.cateName = :cateName")
    int UpdateCateIdx(String cateName, int cateIdx);

    @Transactional
    @Modifying
    @Query("delete from Categories c where c.cateUuid = :cateUuid")
    int deleteByCategoriId(Long cateUuid);


    @Transactional
    @Modifying
    @Query("delete from Categories c where c.userUuid = :userUuid")
    void deleteAllByUserUuid(@Param("userUuid") Long userUuid);

    @Transactional
    @Modifying
    @Query("delete from Categories c where c.pBlog.pUuid = :pUuid")
    void deleteByPBlogUuid(@Param("pUuid") Long pUuid);
}