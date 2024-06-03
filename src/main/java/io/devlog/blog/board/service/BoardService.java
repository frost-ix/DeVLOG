package io.devlog.blog.board.service;

import io.devlog.blog.board.entity.Board;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface BoardService {
    ResponseEntity<?> getBoards();

    ResponseEntity<?> getCategories();
    
    Optional<Board> getBoard(String id);

    Board create(Board board);

    Board update(Board board);

    void delete(String id);

    void deleteAll();

    boolean exists(String id);

    long count();
}