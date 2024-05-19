package io.devlog.blog.user.repository;

import io.devlog.blog.user.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findOneById(String userId);

    void deleteById(@NonNull String id);
}
