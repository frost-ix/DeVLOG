package io.devlog.blog.user.repository;

import io.devlog.blog.user.entity.Subscribes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribesRepository extends JpaRepository<Subscribes, Long> {
    @Query("SELECT s FROM Subscribes s WHERE s.user.userUuid =:userUuid")
    List<Subscribes> findByUserUuid(@Param("userUuid") long userUuid);

    @Query("select 1 from Subscribes s where s.subUser = :subUser")
    boolean existsBySubUserUuid(@Param("subUser") long subUserUuid);
}