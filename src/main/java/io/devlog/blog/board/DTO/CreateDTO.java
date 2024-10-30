package io.devlog.blog.board.DTO;

import io.devlog.blog.board.entity.Categories;
import lombok.Builder;

public class CreateDTO {
    private final String cateName;
    private final int cateIdx;

    @Builder
    public CreateDTO(String cateName, int cateIdx) {
        this.cateName = cateName;
        this.cateIdx = cateIdx;
    }

    public Categories toEntity() {
        return Categories.builder()
                .cateName(cateName)
                .cateIdx(cateIdx)
                .build();
    }
}
