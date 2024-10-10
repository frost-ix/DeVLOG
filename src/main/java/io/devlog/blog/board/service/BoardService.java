package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.BoardDTO;
import io.devlog.blog.board.entity.Board;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface BoardService {
    ResponseEntity<?> getBoards();

    ResponseEntity<?> getCategories();

    ResponseEntity<?> getBoard(Long id);

    ResponseEntity<?> create(BoardDTO boardDTO);

    ResponseEntity<?> update(BoardDTO boardDTO);

    ResponseEntity<?> deleteBoard(Long id);

    ResponseEntity<?> deleteAll();

    boolean exists(String id);

    long count();
}