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

    @Query("select c.cateName from Categories c where c.userUuid = :userUuid")
    List<String> findByUserCateName(Long userUuid);

    Optional<Categories> findByCateName(String cateName);

    @Transactional
    @Modifying
    @Query("delete from Categories c where c.userUuid = :userUuid")
    void deleteAllByUserUuid(@Param("userUuid") Long userUuid);
}