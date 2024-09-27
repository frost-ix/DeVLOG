package io.devlog.blog.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.devlog.blog.user.DTO.SubscribesDTO;
import io.devlog.blog.user.entity.QSubscribes;
import io.devlog.blog.user.entity.Subscribes;
import io.devlog.blog.user.repository.SubscribesRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class UserSubServiceImpl extends QuerydslRepositorySupport implements UserSubService {
    private final SubscribesRepository sbRepo;
    private final JPAQueryFactory jpaqf;

    public UserSubServiceImpl(final SubscribesRepository sbRepo, final JPAQueryFactory jpaqf) {
        super(Subscribes.class);
        this.sbRepo = sbRepo;
        this.jpaqf = jpaqf;
    }

    @Override
    public ResponseEntity<?> getUserSubs() {
        try {
            return ResponseEntity.ok(sbRepo.findAll());
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().body(e);
        }
    }

    @Override
    public ResponseEntity<?> getUserSub(long subUuid) {
        try {
            return ResponseEntity.ok(sbRepo.findById(subUuid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @Override
    public ResponseEntity<?> addUserSub(SubscribesDTO sbDTO) {
        try {
            return ResponseEntity.ok(sbRepo.save(sbDTO.toEntity()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @Override
    public ResponseEntity<?> deleteUserSub(long subUuid) {
        try {
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
