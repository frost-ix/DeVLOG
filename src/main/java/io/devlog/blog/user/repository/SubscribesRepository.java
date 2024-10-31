package io.devlog.blog.user.repository;

import io.devlog.blog.user.entity.Subscribes;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribesRepository extends JpaRepository<Subscribes, Long> {
    @Query("SELECT s FROM Subscribes s WHERE s.user.userUuid =:userUuid")
    List<Subscribes> findByUserUuid(@Param("userUuid") long userUuid);

    @Query("SELECT s FROM Subscribes s WHERE s.subUserId =:subUserId")
    Subscribes findBySubUser(@Param("subUserId") String subUserId);

    @Query("select count(s) from Subscribes s where s.subUserId =:subUserId")
    int countBySubUserId(@Param("subUserId") String subUserId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Subscribes s WHERE s.user.userUuid =:userUuid")
    void deleteAllByUserUuid(@Param("userUuid") long userUuid);
}