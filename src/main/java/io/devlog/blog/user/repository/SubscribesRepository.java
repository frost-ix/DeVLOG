package io.devlog.blog.user.repository;

import io.devlog.blog.user.entity.Subscribes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribesRepository extends JpaRepository<Subscribes, Long> {
}