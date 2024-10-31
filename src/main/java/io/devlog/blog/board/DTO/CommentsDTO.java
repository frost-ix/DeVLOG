package io.devlog.blog.board.DTO;

import io.devlog.blog.board.entity.Comments;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentsDTO {
    private Long commentUuid;
    private String comments;
    private String imagePath;
    private Long boardUuid;
    private Long userUuid;
    private String userName;
    private LocalDateTime commentDate;
    private String userIcon;

    @Builder
    public CommentsDTO(@Nullable Long commentUuid, @Nullable String comments, @Nullable String imagePath, @Nullable Long boardUuid, @Nullable Long userUuid, @Nullable String userName, @Nullable LocalDateTime commentDate, @Nullable String userIcon) {
        this.commentUuid = commentUuid;
        this.comments = comments;
        this.imagePath = imagePath;
        this.boardUuid = boardUuid;
        this.userUuid = userUuid;
        this.userName = userName;
        this.commentDate = commentDate;
        this.userIcon = userIcon;
    }

    public static CommentsDTO toDTO(Comments comments) {
        return CommentsDTO.builder()
                .commentUuid(comments.getCommentUuid())
                .comments(comments.getComments())
                .imagePath(comments.getImagePath())
                .boardUuid(comments.getBoard().getBoardUuid())
                .userName(comments.getUserName())
                .commentDate(comments.getCommentDate())
                .userIcon(null)
                .build();
    }

    public Comments toEntity() {
        return Comments.builder()
                .commentUuid(commentUuid)
                .comments(comments)
                .imagePath(imagePath)
                .userUuid(userUuid)
                .userName(userName)
                .board(null)
                .commentDate(commentDate)
                .build();
    }
}