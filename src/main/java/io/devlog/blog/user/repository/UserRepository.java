package io.devlog.blog.user.repository;

import io.devlog.blog.user.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByBenderUuid(String benderUuid);

    void deleteById(@NonNull Long userUuid);

}