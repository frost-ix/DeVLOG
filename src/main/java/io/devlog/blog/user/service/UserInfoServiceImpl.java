package io.devlog.blog.user.service;

import io.devlog.blog.config.CustomException;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.user.DTO.UserInfoDTO;
import io.devlog.blog.user.entity.UserInfo;
import io.devlog.blog.user.repository.UserInfoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserInfoServiceImpl extends QuerydslRepositorySupport implements UserInfoService {
    private final UserInfoRepository userInfoRepository;

    public UserInfoServiceImpl(final UserInfoRepository userInfoRepository) {
        super(UserInfo.class);
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public ResponseEntity<?> getUserInfo(long userUuid) {
        return ResponseEntity.ok(userInfoRepository.findById(userUuid));
    }

    @Override
    public ResponseEntity<?> updateUserInfo(long userUuid, UserInfoDTO info) {
        try {
            if (info == null) {
                log.error("Cannot find data");
                throw new CustomException(Status.NO_CONTENT);
            }
            if (userInfoRepository.findById(userUuid).isPresent()) {
                int c = userInfoRepository.updateInformation(userUuid, info.toEntity());
                if (c == 0) {
                    log.error("Cannot update user information");
                    throw new CustomException(Status.NOT_MODIFIED);
                } else {
                    return ResponseEntity.ok().body(Status.OK);
                }
            }
            UserInfo userInfo = info.toEntity();
            userInfoRepository.save(userInfo);
            return ResponseEntity.ok().body(Status.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(Status.SERVER_ERROR);
        }
    }
}
