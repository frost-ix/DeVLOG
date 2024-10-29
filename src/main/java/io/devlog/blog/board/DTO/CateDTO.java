package io.devlog.blog.board.DTO;


import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.pblog.Entity.PBlog;
import io.devlog.blog.team.entity.TBlog;
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
    private PBlog pBlog;
    private TBlog tBlog;

    @Builder
    public CateDTO(@Nullable String cateName, @Nullable Long userUuid, @Nullable Long cateUuid, @Nullable int cateIdx, @Nullable PBlog pBlog, @Nullable TBlog tBlog) {
        this.cateName = cateName;
        this.userUuid = userUuid;
        this.cateUuid = cateUuid;
        this.cateIdx = cateIdx;
        this.pBlog = pBlog;
        this.tBlog = tBlog;

    }

    public static CateDTO toDTO(Categories categories) {
        return CateDTO.builder()
                .cateName(categories.getCateName())
                .cateUuid(categories.getCateUuid())
                .cateIdx(categories.getCateIdx())
                .build();
    }

    public static CateDTO ofPBlog(String cateName, Long userUuid, Long cateUuid, int cateIdx, PBlog pBlog, TBlog tBlog) {
        return new CateDTO(cateName, userUuid, cateUuid, cateIdx, pBlog, null);
    }

    public static CateDTO ofTBlog(String cateName, Long userUuid, Long cateUuid, int cateIdx, PBlog pBlog, TBlog tBlog) {
        return new CateDTO(cateName, userUuid, cateUuid, cateIdx, null, tBlog);
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
