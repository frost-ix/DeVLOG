package io.devlog.blog.user.repository;

import io.devlog.blog.user.entity.UserInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE UserInfo u " +
            "set u.userGit = :userGit, u.userIcon = :userIcon, u.userInsta = :userInsta, u.userSummary = :userSummary, u.userX = :userX" +
            " WHERE u.user.userUuid = :userUuid")
    int updateInformation(@Param("userUuid") long userUuid,
                          @Param("userIcon") String userIcon, @Param("userSummary") String userSummary,
                          @Param("userGit") String userGit, @Param("userX") String userX, @Param("userInsta") String userInsta);

    @Query("SELECT u FROM UserInfo u WHERE u.user.userUuid = :userUuid")
    UserInfo findByUserUuid(@Param("userUuid") long userUuid);

    @Query("SELECT u.userIcon FROM UserInfo u WHERE u.user.userUuid = :userUuid")
    String findUserInfoByUserUuid(@Param("userUuid") long userUuid);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserInfo u WHERE u.user.userUuid = :userUuid")
    void deleteByUserUuid(@Param("userUuid") long userUuid);
}
