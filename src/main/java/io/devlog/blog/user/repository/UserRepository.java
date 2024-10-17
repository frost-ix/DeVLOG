package io.devlog.blog.user.repository;

import io.devlog.blog.user.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUserId(String userId);

    Optional<User> findOneByUserUuid(Long userUuid);

    @Query("SELECT u from User u where u.userUuid = :userUuid")
    Optional<User> findByUserUuid(Long userUuid);

    Optional<User> findOneByBenderUuid(String benderUuid);

    void deleteById(@NonNull Long userUuid);
}