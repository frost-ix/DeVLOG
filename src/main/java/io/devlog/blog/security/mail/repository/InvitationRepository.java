package io.devlog.blog.security.mail.repository;

import io.devlog.blog.security.mail.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    @Query("select i from Invitation i where i.code = :code")
    Optional<Invitation> findInvitationByCode(@Param("code") String code);
}
