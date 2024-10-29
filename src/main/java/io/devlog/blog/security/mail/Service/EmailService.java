package io.devlog.blog.security.mail.Service;

import io.devlog.blog.config.enums.ExceptionStatus;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.security.mail.DTO.VerifyCode;
import io.devlog.blog.security.mail.entity.Invitation;
import io.devlog.blog.security.mail.enums.InvitationStatus;
import io.devlog.blog.security.mail.repository.InvitationRepository;
import io.devlog.blog.team.entity.TBlog;
import io.devlog.blog.team.entity.TBlogRole;
import io.devlog.blog.team.repository.TBlogRepository;
import io.devlog.blog.team.repository.TBlogRoleRepository;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class EmailService {
    private final JavaMailSender mailSender;
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final TBlogRepository tBlogRepository;
    private final TBlogRoleRepository tBlogRoleRepository;

    public EmailService(JavaMailSender mailSender,
                        InvitationRepository invitationRepository,
                        UserRepository userRepository,
                        TBlogRepository tBlogRepository,
                        TBlogRoleRepository tBlogRoleRepository) {
        this.mailSender = mailSender;
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.tBlogRepository = tBlogRepository;
        this.tBlogRoleRepository = tBlogRoleRepository;
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }

    public ResponseEntity<?> sendEmail(Invitation invitation) {
        invitation.setCode(generateCode());
        Optional<User> sender = userRepository.findOneByName(invitation.getSender());
        Optional<User> receiver = userRepository.findOneByName(invitation.getReceiver());
        if (sender.isEmpty() || receiver.isEmpty()) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
        log.info("sender: {}", sender.get().getMail());
        log.info("receiver: {}", receiver.get().getMail());
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(sender.get().getMail());
            helper.setTo(receiver.get().getMail());
            helper.setSubject("초대 메일 입니다.");
            helper.setText("승인 코드 : " + invitation.getCode(), true);
            mailSender.send(message);
            invitation.setStatus(InvitationStatus.PENDING);
            Invitation i = invitationRepository.save(invitation);
            i.setSender(sender.get().getName());
            i.setReceiver(receiver.get().getName());
            invitationRepository.save(i);
            return ResponseEntity.ok().body(Status.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> verifyEmail(VerifyCode code) {
        log.info("code: {}", code);
        Optional<Invitation> invitation = invitationRepository.findByCode(code.getCode());
        if (invitation.isEmpty() || invitation.get().getStatus().equals(InvitationStatus.ACCEPTED)) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            invitation.get().setStatus(InvitationStatus.ACCEPTED);
            invitationRepository.save(invitation.get());
            Optional<User> sender = userRepository.findOneByName(invitation.get().getSender());
            Optional<User> receiver = userRepository.findOneByName(invitation.get().getReceiver());
            if (sender.isEmpty() || receiver.isEmpty()) {
                return ResponseEntity.badRequest().body(ExceptionStatus.NOT_FOUND);
            }
            if (tBlogRoleRepository.existsTBlogRoleByUserUuid(receiver.get().getUserUuid())) {
                return ResponseEntity.badRequest().body("이미 팀원입니다.");
            }
            TBlog tBlog = tBlogRepository.findTBlogByUserUuid(sender.get().getUserUuid());
            TBlogRole tBlogRole = TBlogRole.builder()
                    .tBlog(tBlog)
                    .userUuid(receiver.get().getUserUuid())
                    .teamRole("팀원")
                    .memberDescription("팀원 입니다.")
                    .build();
            tBlogRoleRepository.save(tBlogRole);
            return ResponseEntity.ok().body(Status.OK);
        }
    }
}
