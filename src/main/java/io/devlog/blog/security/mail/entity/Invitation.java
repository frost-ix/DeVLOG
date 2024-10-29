package io.devlog.blog.security.mail.entity;

import io.devlog.blog.security.mail.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    private String receiver;

    private String code;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;
}
