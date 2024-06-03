package io.devlog.blog.board.api;

import io.devlog.blog.board.service.BoardServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;

@Log4j2
@RestController
@RequestMapping("/board")
public class boardAPI {
    private final BoardServiceImpl boardService;
    @Autowired
    private ResourceLoader resourceLoader;

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
        return boardService.getBoards();
    }

    @GetMapping("/cate")
    public ResponseEntity<?> getCategories() {
        return boardService.getCategories();
    }

    public ResponseEntity<?> getBoardDetail() {
        return null;
    }

    public ResponseEntity<?> createBoard() {
        return null;
    }

    public ResponseEntity<?> updateBoard() {
        return null;
    }

    public ResponseEntity<?> deleteBoard() {
        return null;
    }
}