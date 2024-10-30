package io.devlog.blog.board.DTO;

import io.devlog.blog.board.entity.Board;
import io.devlog.blog.board.entity.BoardTags;
import io.devlog.blog.board.entity.Categories;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String boardProfilepath;
    private int visitCount;

    @Builder
    public BoardDTO(@Nullable Long boardUuid, @Nullable String categories, @Nullable String title, @Nullable String content, @Nullable List<String> tags, @Nullable String userName, @Nullable Long userUuID, @Nullable String boardProfilepath, @Nullable int visitCount) {
        this.boardUuid = boardUuid;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.userName = userName;
        this.userUuID = userUuID;
        this.categories = categories;
        this.boardProfilepath = boardProfilepath;
        this.visitCount = visitCount;

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
                .boardProfilepath(board.getBoardProfilepath())
                .visitCount(board.getVisitCount())
                .build();
    }

    public Board toEntity(Categories categories, List<BoardTags> boardTags) {
        return Board.builder()
                .boardUuid(boardUuid)
                .boardTitle(title)
                .boardContent(content)
                .boardProfilepath(boardProfilepath)
                .visitCount(visitCount)
                .categories(categories)
                .boardTags(boardTags)
                .userName(userName)
                .userUuid(userUuID)
                .build();

    }
}