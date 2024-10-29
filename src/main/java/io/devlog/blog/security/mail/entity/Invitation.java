package io.devlog.blog.security.mail.entity;

import io.devlog.blog.security.mail.enums.InvitationStatus;
import io.devlog.blog.user.entity.User;
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

    @ManyToOne
    private User senderUser;
    
    @ManyToOne
    private User receiverUser;

    private String code;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;
}
