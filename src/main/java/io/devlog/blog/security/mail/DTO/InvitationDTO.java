package io.devlog.blog.security.mail.DTO;

import lombok.Data;

@Data
public class InvitationDTO {
    private String sender;

    private String receiver;
}
