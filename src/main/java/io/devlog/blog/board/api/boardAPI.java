package io.devlog.blog.board.api;

import io.devlog.blog.board.DTO.BoardDTO;
import io.devlog.blog.board.service.BoardServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/board")
public class boardAPI {
    private final BoardServiceImpl boardService;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private BoardServiceImpl boardServiceImpl;

    public boardAPI(BoardServiceImpl boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getBoardList() {
        log.info("Get boards");
        return boardService.getBoards();
    }

    @GetMapping("/cate")
    public ResponseEntity<?> getCategories() {
        return boardService.getCategories();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getBoard(@PathVariable Long id) {
        log.info("Get board : {}", id);
        return boardService.getBoard(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBoard(@RequestBody BoardDTO boardDTO) {
        log.info("Create board : {}", boardDTO);
        return boardService.create(boardDTO);

    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateBoard(@RequestBody BoardDTO boardDTO) {
        log.info("Update board : {}", boardDTO);
        return boardService.update(boardDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        log.info("Delete board : {}", id);
        return boardServiceImpl.deleteBoard(id);

    }
}