package io.devlog.blog.user.DTO;

import io.devlog.blog.user.entity.Subscribes;
import io.devlog.blog.user.entity.User;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscribesDTO {
    private Long userUuid;
    private Long id;
    private Long subUser;
    private LocalDateTime subDate = LocalDateTime.now();

    @Builder
    public SubscribesDTO(@Nullable Long userUuid, Long id, @Nullable Long subUser, @Nullable LocalDateTime subDate) {
        this.userUuid = userUuid;
        this.id = id;
        this.subUser = subUser;
        this.subDate = subDate;
    }

    public SubscribesDTO toDTO(Subscribes subscribes) {
        return SubscribesDTO.builder()
                .id(subscribes.getId())
                .subUser(subscribes.getSubUser())
                .subDate(subscribes.getSubDate())
                .userUuid(subscribes.getUser().getUserUuid())
                .build();
    }

    public Subscribes toEntity() {
        return Subscribes.builder()
                .id(userUuid)
                .subUser(subUser)
                .subDate(subDate)
                .user(User.builder().userUuid(userUuid).build())
                .build();
    }
}