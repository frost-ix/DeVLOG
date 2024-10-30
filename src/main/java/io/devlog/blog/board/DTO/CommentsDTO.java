package io.devlog.blog.board.DTO;

import io.devlog.blog.board.entity.Comments;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentsDTO {
    private String comments;
    private String imagePath;
    private Long boardUuid;
    private Long userUuid;
    private String userName;

    @Builder
    public CommentsDTO(@Nullable String comments, @Nullable String imagePath, @Nullable Long boardUuid, @Nullable Long userUuid, @Nullable String userName) {
        this.comments = comments;
        this.imagePath = imagePath;
        this.boardUuid = boardUuid;
        this.userUuid = userUuid;
        this.userName = userName;
    }

    public static CommentsDTO toDTO(Comments comments) {
        return CommentsDTO.builder()
                .comments(comments.getComments())
                .imagePath(comments.getImagePath())
                .boardUuid(comments.getBoard().getBoardUuid())
                .userUuid(comments.getUserUuid())
                .userName(comments.getUserName())
                .build();
    }

    public Comments toEntity() {
        return Comments.builder()
                .comments(comments)
                .imagePath(imagePath)
                .userUuid(userUuid)
                .userName(userName)
                .board(null)
                .build();
    }
}