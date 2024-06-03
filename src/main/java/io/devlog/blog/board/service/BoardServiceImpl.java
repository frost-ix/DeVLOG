package io.devlog.blog.board.service;

import io.devlog.blog.board.entity.Board;
import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.board.repository.BoardRepository;
import io.devlog.blog.board.repository.CateRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final CateRepository cateRepository;

    public BoardServiceImpl(BoardRepository boardRepository, CateRepository cateRepository) {
        this.boardRepository = boardRepository;
        this.cateRepository = cateRepository;
    }

    @Override
    public ResponseEntity<?> getBoards() {
        try {
            Optional<List<Board>> boards = Optional.of(boardRepository.findAll());
            return ResponseEntity.ok(boards);
        } catch (Exception e) {
            log.error("get boards error", e);
            return ResponseEntity.badRequest().body("get boards error");
        }
    }

    @Override
    public ResponseEntity<?> getCategories() {
        try {
            Optional<List<Categories>> categories = Optional.of(cateRepository.findAll());
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            log.error("get categories error", e);
            return ResponseEntity.badRequest().body("get categories error");
        }
    }

    @Override
    public Optional<Board> getBoard(String id) {
        try {
            return boardRepository.findById(id);
        } catch (Exception e) {
            log.error("get board by id error", e);
            return Optional.empty();
        }
    }

    @Override
    public Board create(Board board) {
        boardRepository.save(board);
        return board;
    }

    @Override
    public Board update(Board board) {
        boardRepository.save(board);
        return board;
    }

    @Override
    public void delete(String id) {
        boardRepository.deleteById(id);
        log.info("delete board by id: {}", id);
    }

    @Override
    public void deleteAll() {
        boardRepository.deleteAll();
        log.info("delete all boards");
    }

    @Override
    public boolean exists(String id) {
        return boardRepository.existsById(id);
    }

    @Override
    public long count() {
        return boardRepository.count();
    }
}