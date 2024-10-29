package io.devlog.blog.board.DTO;


import io.devlog.blog.board.entity.Board;
import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.board.entity.BoardTags;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CateDTO {
    private String cateName;
    private Long userUuid;
    private Long cateUuid;
    private int cateIdx;

    @Builder
    public CateDTO(@Nullable String cateName, @Nullable Long userUuid, @Nullable Long cateUuid, @Nullable int cateidx) {
        this.cateName = cateName;
        this.userUuid = userUuid;
        this.cateUuid = cateUuid;
        this.cateIdx = cateIdx;
    }

    public Categories toEntity() {
        return Categories.builder()
                .cateName(cateName)
                .userUuid(userUuid)
                .cateidx(cateIdx)
                .build();
    }
}
