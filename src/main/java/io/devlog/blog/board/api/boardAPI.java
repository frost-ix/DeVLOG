package io.devlog.blog.board.api;

import io.devlog.blog.board.DTO.BoardDTO;
import io.devlog.blog.board.entity.Board;
import io.devlog.blog.board.service.BoardServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.util.Optional;

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

    @GetMapping("/")
    public ResponseEntity<String> index() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:static/index.html");
        String htmlContent = Files.readString(resource.getFile().toPath());
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlContent);
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
    @GetMapping("/detail")
    public Optional<Board> getBoard(@RequestBody Long id) {
        log.info("Get board : {}", id);
        return boardService.getBoard(id);
    }

    @GetMapping("/create")
    public ResponseEntity<?> createBoard(@RequestBody BoardDTO boardDTO) {
        try {
            System.out.println(boardDTO);
            Board createdBoard = boardService.create(boardDTO);
            return ResponseEntity.ok(createdBoard);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body("Create board error");
        }
    }

    @GetMapping("/update")
    public ResponseEntity<?> updateBoard() {
        return null;
    }
    @GetMapping("/delete")
    public ResponseEntity<?> deleteBoard(@RequestBody String id) {
        boardService.deleteBoard(id);
        return null;
    }
}