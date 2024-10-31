package io.devlog.blog.security.mail.Service;

import io.devlog.blog.config.enums.ExceptionStatus;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.security.mail.DTO.InvitationDTO;
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

    public ResponseEntity<?> sendEmail(InvitationDTO invitation) {
        Optional<User> sender = userRepository.findOneByUserId(invitation.getSender());
        Optional<User> receiver = userRepository.findOneByUserId(invitation.getReceiver());
        if (receiver.isEmpty()) {
            return ResponseEntity.badRequest().body(ExceptionStatus.NOT_FOUND);
        }
        if (sender.isEmpty()) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
        if (sender.get().getName().equals(receiver.get().getName())) {
            return ResponseEntity.badRequest().body("자신을 초대할 수 없습니다.");
        }
        log.info("sender: {}", sender.get().getMail());
        log.info("receiver: {}", receiver.get().getMail());
        Invitation i = new Invitation();
        i.setCode(generateCode());
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(sender.get().getMail());
            helper.setTo(receiver.get().getMail());
            helper.setSubject("DeVLOOG 팀 초대 메일 입니다.");
            helper.setText("팀 블로그 이름 : " + tBlogRepository.findTBlogByUserUuid(sender.get().getUserUuid()).getTName() + "\n" +
                    "초대 한 사람 : " + sender.get().getName() + "\n" +
                    "승인 코드 : " + i.getCode(), true);
            mailSender.send(message);
            i.setStatus(InvitationStatus.PENDING);
            Invitation io = invitationRepository.save(i);
            io.setSender(sender.get().getName());
            io.setReceiver(receiver.get().getName());
            invitationRepository.save(io);
            return ResponseEntity.ok().body(Status.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> verifyEmail(VerifyCode code) {
        log.info("code: {}", code.getCode());
        Optional<Invitation> invitation = invitationRepository.findInvitationByCode(code.getCode());
        if (invitation.isEmpty() || invitation.get().getStatus().equals(InvitationStatus.ACCEPTED)) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        } else {
            Invitation i = invitation.get();
            i.setStatus(InvitationStatus.ACCEPTED);
            invitationRepository.save(i);
            Optional<User> sender = userRepository.findOneByName(i.getSender());
            Optional<User> receiver = userRepository.findOneByName(i.getReceiver());
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
                    .teamRole("MEMBER")
                    .userIcon(receiver.get().getUserInfo().getUserIcon())
                    .memberDescription("팀원 입니다.")
                    .build();
            tBlogRoleRepository.save(tBlogRole);
            return ResponseEntity.ok().body(Status.OK);
        }
    }
}
