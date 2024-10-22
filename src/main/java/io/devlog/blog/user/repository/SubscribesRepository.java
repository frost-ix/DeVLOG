package io.devlog.blog.user.repository;

import io.devlog.blog.user.entity.Subscribes;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribesRepository extends JpaRepository<Subscribes, Long> {
    @Query("SELECT s FROM Subscribes s WHERE s.user.userUuid =:userUuid")
    List<Subscribes> findByUserUuid(@Param("userUuid") long userUuid);

    boolean existsBySubUser(long subUser);

    @Transactional
    void deleteBySubUser(long subUser);
}