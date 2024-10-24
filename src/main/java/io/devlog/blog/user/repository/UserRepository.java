package io.devlog.blog.user.repository;

import io.devlog.blog.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUserId(String userId);

    Optional<User> findOneByUserUuid(Long userUuid);

    @Query("SELECT u from User u where u.userUuid = :userUuid")
    Optional<User> findByUserUuid(Long userUuid);

    @Transactional
    @Modifying
    @Query("update User u " +
            " set u.userPw = :pw, u.name = :name, u.mail = :mail where u.userUuid = :userUuid")
    int updateUserByUserUuid(@Param("userUuid") Long userUuid,
                             @Param("pw") String pw,
                             @Param("name") String name,
                             @Param("mail") String mail);

    boolean existsByUserUuid(Long userId);

    Optional<User> findOneByBenderUuid(String benderUuid);

    void deleteById(@NonNull Long userUuid);
}