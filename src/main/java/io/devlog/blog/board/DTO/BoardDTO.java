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

    public static BoardDTO fromEntity(Board board) {
        return BoardDTO.builder()
                .boardUuid(board.getBoardUuid())
                .categories(board.getCategories().getCateName())
                .title(board.getBoardTitle())
                .content(board.getBoardContent())
                .tags(board.getBoardTags().stream()
                        .map(boardTag -> boardTag.getTag().getTagName())
                        .collect(Collectors.toList()))
                .userName(board.getUserName())
                .userUuID(board.getUserUuid())
                .build();
    }

    public Board toEntity(Categories categories, List<BoardTags> boardTags) {
        return Board.builder()
                .boardTitle(title)
                .boardContent(content)
                .categories(categories)
                .boardTags(boardTags)
                .userName(userName)
                .userUuid(userUuID)
                .build();
    }
}