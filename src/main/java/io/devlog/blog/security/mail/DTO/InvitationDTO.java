package io.devlog.blog.security.mail.DTO;

import lombok.Getter;

@Getter
public class InvitationDTO {
    private String sender;

    private String receiver;
}
