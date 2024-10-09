package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.BoardDTO;
import io.devlog.blog.board.entity.Board;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface BoardService {
    ResponseEntity<?> getBoards();

    ResponseEntity<?> getCategories();
    
    Optional<Board> getBoard(Long id);

    Board create(BoardDTO boardDTO);

    Board update(BoardDTO boardDTO);

    void deleteBoard(Long id);

    void deleteAll();

    boolean exists(String id);

    long count();
}