package io.devlog.blog.user.DTO;

import io.devlog.blog.user.entity.Subscribes;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscribesDTO {
    private Long id;
    private String subUser;
    private LocalDateTime subDate = LocalDateTime.now();

    @Builder
    public SubscribesDTO(@Nullable Long id, @Nullable String subUser, @Nullable LocalDateTime subDate) {
        this.id = id;
        this.subUser = subUser;
        this.subDate = subDate;
    }

    public SubscribesDTO toDTO(Subscribes subscribes) {
        return SubscribesDTO.builder()
                .id(subscribes.getId())
                .subUser(subscribes.getSubUser())
                .subDate(subscribes.getSubDate())
                .build();
    }

    public Subscribes toEntity() {
        return Subscribes.builder()
                .id(id)
                .subUser(subUser)
                .subDate(subDate)
                .build();
    }
}