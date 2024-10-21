package io.devlog.blog.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.devlog.blog.config.CustomException;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.user.DTO.SubscribesDTO;
import io.devlog.blog.user.DTO.UserDTO;
import io.devlog.blog.user.entity.QSubscribes;
import io.devlog.blog.user.entity.Subscribes;
import io.devlog.blog.user.entity.User;
import io.devlog.blog.user.repository.SubscribesRepository;
import io.devlog.blog.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class UserSubServiceImpl extends QuerydslRepositorySupport implements UserSubService {
    private final SubscribesRepository sbRepo;
    private final UserRepository userRepo;
    private final JPAQueryFactory jpaqf;

    public UserSubServiceImpl(final SubscribesRepository sbRepo, final UserRepository userRepo, final JPAQueryFactory jpaqf) {
        super(Subscribes.class);
        this.sbRepo = sbRepo;
        this.userRepo = userRepo;
        this.jpaqf = jpaqf;
    }

    @Override
    public ResponseEntity<?> getUsersSub() {
        try {
            return ResponseEntity.ok(sbRepo.findAll());
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().body(e);
        }
    }

    @Override
    public ResponseEntity<?> getUsersSubCount(long userUuid) {
        long count = sbRepo.findByUserUuid(userUuid).size();
        if (count == 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(count);
        }
    }

    @Override
    public ResponseEntity<?> getUserSub(long userUuid) {
        try {
            List<Subscribes> finds = sbRepo.findByUserUuid(userUuid);
            if (finds.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                HashMap<Long, UserDTO> subUsers = new HashMap<>();
                finds.forEach((s) -> {
                    Optional<User> i = userRepo.findByUserUuid(s.getSubUser());
                    if (i.isPresent()) {
                        UserDTO e = UserDTO.toDTO(i.get());
                        e.setPw(null);
                        e.setMail(null);
                        e.setBender(null);
                        e.setBenderUuid(null);
                        subUsers.put(i.get().getUserUuId(), e);
                    }
                });
                return ResponseEntity.ok(subUsers);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @Override
    public ResponseEntity<?> addUserSub(long userUuid, SubscribesDTO sbDTO) {
        try {
            sbDTO.setUserUuid(userUuid);
            if (sbRepo.existsBySubUserUuid(sbDTO.getSubUser())) {
                throw new CustomException(Status.CONFLICT);
            }
            return ResponseEntity.ok(sbRepo.save(sbDTO.toEntity()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @Override
    public ResponseEntity<?> deleteUserSub(long subUuid) {
        try {
            if (!sbRepo.existsBySubUserUuid(subUuid)) {
                return ResponseEntity.badRequest().body(new CustomException(Status.NO_CONTENT));
            }
            sbRepo.deleteById(subUuid);
            return ResponseEntity.ok("Deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @Override
    public ResponseEntity<?> deleteUserSubs(long userUuid) {
        try {
            List<Subscribes> listSub = jpaqf.select(QSubscribes.subscribes)
                    .from(QSubscribes.subscribes)
                    .where(QSubscribes.subscribes.user.userUuid.eq(userUuid))
                    .stream().toList();
            return ResponseEntity.ok(listSub);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}
