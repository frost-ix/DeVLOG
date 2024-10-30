package io.devlog.blog.user.DTO;

import io.devlog.blog.user.entity.Subscribes;
import io.devlog.blog.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscribesDTO {
    private String subUserId;
    private LocalDateTime subDate = LocalDateTime.now();

    @Builder
    public SubscribesDTO(String subUserId, LocalDateTime subDate) {
        this.subUserId = subUserId;
        this.subDate = subDate;
    }

    public Subscribes toEntity(User user) {
        return Subscribes.builder()
                .user(user)
                .subUserId(subUserId)
                .subDate(subDate)
                .build();
    }
}