package io.devlog.blog.board.DTO;

import io.devlog.blog.board.entity.Board;
import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.board.entity.Images;
import io.devlog.blog.board.entity.Tags;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.List;
import java.util.Objects;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDTO {
    private Long boardUuid;
    private String categories;
    private String title;
    private String content;
    private List<String> tags;
    private String userName;
    private Long userUuID;

    @Builder
    public BoardDTO(@Nullable Long boardUuid, @Nullable String categories, @Nullable String title, @Nullable String content, @Nullable List<String> tags, @Nullable String userName, @Nullable Long userUuID) {
        this.boardUuid = boardUuid;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.userName = userName;
        this.userUuID = userUuID;
        this.categories = categories;
    }

//    public BoardDTO toDTO(Board board) {
//        return BoardDTO.builder()
//                .boardUuid(Objects.requireNonNull(board.getBoardUuid()))
//                .categories(Objects.requireNonNull(board.getCategories()))
//                .title(Objects.requireNonNull(board.getBoardTitle()))
//                .content(Objects.requireNonNull(board.getBoardContent()))
//                .tags(Objects.requireNonNull(board.getTags()))
//                .userUuID(Objects.requireNonNull(board.getUserUuid()))
//                .build();
//
//    }

    public Board toEntity(Categories categories, List<Tags> tags) {
        return Board.builder()
                .boardUuid(boardUuid)
                .boardTitle(title)
                .boardContent(content)
                .categories(categories)
                .tags(tags)
                .userName(userName)
                .userUuid(userUuID)
                .build();
    }
}
