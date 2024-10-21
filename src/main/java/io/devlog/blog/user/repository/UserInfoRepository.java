package io.devlog.blog.user.repository;

import io.devlog.blog.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    @Query("UPDATE UserInfo u " +
            " set  u = :info, u.user.userUuid = :userUuid" +
            " WHERE u.user.userUuid = :userUuid")
    int updateInformation(@Param("userUuid") long userUuid, @Param("info") UserInfo info);
}
