package io.devlog.blog.security.mail.api;

import io.devlog.blog.security.mail.DTO.VerifyCode;
import io.devlog.blog.security.mail.Service.EmailService;
import io.devlog.blog.security.mail.entity.Invitation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailAPI {
    private final EmailService emailService;

    public MailAPI(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/invite")
    public ResponseEntity<?> invite(@RequestBody Invitation invitation) {
        return emailService.sendEmail(invitation);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyCode code) {
        return emailService.verifyEmail(code);
    }
}
