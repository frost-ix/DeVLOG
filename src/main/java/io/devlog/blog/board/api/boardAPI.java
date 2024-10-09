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
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/detail/{id}")
    public Optional<Board> getBoard(@PathVariable Long id) {
        log.info("Get board : {}", id);
        return boardService.getBoard(id);
    }

    @GetMapping("/create")
    public ResponseEntity<?> createBoard(@RequestBody BoardDTO boardDTO) {
        try {
            System.out.println(boardDTO);
            boardService.create(boardDTO);
            return ResponseEntity.ok(200);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body("Create board error");
        }
    }

    @GetMapping("/update")
    public ResponseEntity<?> updateBoard(@RequestBody BoardDTO boardDTO) {
        try {
            boardService.update(boardDTO);
            return ResponseEntity.ok(200);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body("Update board error");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        try {
            boardServiceImpl.deleteBoard(id);
            System.out.println("delete board by id:"+id);
            return ResponseEntity.ok(200);
        } catch (Exception e) {
            System.out.println("delete board by id:"+id);
            System.out.println(e);
            return ResponseEntity.badRequest().body("Delete board error");
        }
    }
}