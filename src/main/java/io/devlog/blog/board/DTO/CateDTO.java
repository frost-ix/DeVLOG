package io.devlog.blog.board.DTO;


import io.devlog.blog.board.entity.Categories;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class CateDTO {
    private String cateName;
    private Long userUuid;
    private Long cateUuid;
    private int cateIdx;
    private int boardCount;


    @Builder
    public CateDTO(@Nullable String cateName, @Nullable Long userUuid, @Nullable Long cateUuid, @Nullable int cateIdx, @Nullable int boardCount) {
        this.cateName = cateName;
        this.userUuid = userUuid;
        this.cateUuid = cateUuid;
        this.cateIdx = cateIdx;
        this.boardCount = boardCount;


    }

    public static CateDTO toDTO(Categories categories) {
        return CateDTO.builder()
                .cateName(categories.getCateName())
                .cateUuid(categories.getCateUuid())
                .cateIdx(categories.getCateIdx())
                .boardCount(0)
                .build();
    }


    public Categories toEntity() {
        return Categories.builder()
                .cateName(cateName)
                .userUuid(userUuid)
                .cateUuid(cateUuid)
                .cateIdx(cateIdx)
                .build();
    }
}
