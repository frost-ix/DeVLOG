package io.devlog.blog.security.mail.api;

import io.devlog.blog.security.mail.DTO.InvitationDTO;
import io.devlog.blog.security.mail.DTO.VerifyCode;
import io.devlog.blog.security.mail.Service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailAPI {
    private final EmailService emailService;

    public MailAPI(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/invite")
    public ResponseEntity<?> invite(@RequestBody InvitationDTO invitation) {
        return emailService.sendEmail(invitation);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyCode code) {
        return emailService.verifyEmail(code);
    }
}
