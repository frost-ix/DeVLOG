package io.devlog.blog.user.service;

import io.devlog.blog.config.CustomException;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.user.DTO.UserInfoDTO;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.entity.UserInfo;
import io.devlog.blog.user.repository.UserInfoRepository;
import io.devlog.blog.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class UserInfoServiceImpl extends QuerydslRepositorySupport implements UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    public UserInfoServiceImpl(final UserInfoRepository userInfoRepository, UserRepository userRepository) {
        super(UserInfo.class);
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> getUserInfo(long userUuid) {
        return ResponseEntity.ok(userInfoRepository.findByUserUuid(userUuid));
    }


    @Override
    public ResponseEntity<?> updateUserInfo(long userUuid, UserInfoDTO info) {
        try {
            if (info == null) {
                log.error("Cannot find data");
                throw new CustomException(Status.NO_CONTENT);
            }
            UserInfo u = userInfoRepository.findByUserUuid(userUuid);
            if (u == null) {
                Optional<User> user = userRepository.findByUserUuid(userUuid);
                if (user.isEmpty()) {
                    log.error("Cannot find user");
                    throw new CustomException(Status.USER_NOT_FOUND);
                }
                UserInfo ui = info.toEntity();
                ui.setUser(user.get());
                userInfoRepository.save(ui);
                ui.getUser().setUserUuid(userUuid);
                return ResponseEntity.ok().body(Status.OK);
            } else {
                int c = userInfoRepository.updateInformation(
                        userUuid,
                        info.getUserIcon(), info.getUserSummary(),
                        info.getUserGit(), info.getUserX(), info.getUserInsta()
                );
                if (c == 0) {
                    log.error("Cannot update user information");
                    throw new CustomException(Status.NOT_MODIFIED);
                } else {
                    return ResponseEntity.ok().body(Status.OK);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(Status.BAD_REQUEST);
        }
    }
}
