package io.devlog.blog.board.api;

import io.devlog.blog.board.DTO.BoardDTO;
import io.devlog.blog.board.DTO.CommentsDTO;
import io.devlog.blog.board.service.BoardService;
import io.devlog.blog.board.service.CommentsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Log4j2
@RestController
@RequestMapping("/board")
public class boardAPI {
    private final BoardService boardService;
    private final CommentsService commentsService;

    public boardAPI(BoardService boardService, CommentsService commentsService) {
        this.boardService = boardService;
        this.commentsService = commentsService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getBoardList() {
        log.info("Get boards");
        return boardService.getBoards();
    }

    @GetMapping("/p/{pDomain}/{category}")
    public ResponseEntity<?> getCateBoardList(@PathVariable String pDomain, @PathVariable String category) {
        log.info("cateName : {}", category);
        category = URLDecoder.decode(category, StandardCharsets.UTF_8);
        return boardService.getCateBoardList(pDomain, category);
    }

    @GetMapping("/tagboard/{tagName}")
    public ResponseEntity<?> getTagBoardList(@PathVariable String tagName) {
        log.info("Get cate");
        tagName = URLDecoder.decode(tagName, StandardCharsets.UTF_8);
        return boardService.getTagBoardList(tagName);
    }

    @GetMapping("/list")
    public ResponseEntity<?> pagingBoards(@RequestParam("p") int page, @RequestParam("s") int size) {
        log.info("Paging boards : {} {}", page, size);
        return boardService.pagingBoards(page, size);
    }

    @GetMapping("/{cateName}/{cateUuid}")
    public ResponseEntity<?> pagingCateBoards(
            @PathVariable String cateName,
            @PathVariable Long cateUuid,
            @RequestParam("p") int page, @RequestParam("s") int size) {
        cateName = URLDecoder.decode(cateName, StandardCharsets.UTF_8);
        log.info("Paging cate boards : {} {} {} {}", cateName, cateUuid, page, size);
        return boardService.pagingCateBoards(cateUuid, page, size);
    }

    @GetMapping("/user/post")
    public ResponseEntity<?> getUserBoards() {
        return boardService.getUserBoards();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBoard(@PathVariable Long id) {
        log.info("Get board : {}", id);
        return boardService.getBoard(id);
    }

    @GetMapping("/p/{pDomain}")
    public ResponseEntity<?> getBoard(@PathVariable String pDomain) {
        log.info("Get board : {}", pDomain);
        return boardService.getPDomainBoardList(pDomain);
    }

    @GetMapping("/t/{tDomain}")
    public ResponseEntity<?> getTBoard(@PathVariable String tDomain) {
        log.info("Get board : {}", tDomain);
        return boardService.getTDomainBoardList(tDomain);
    }

    @PostMapping("")
    public ResponseEntity<?> createBoard(@RequestBody BoardDTO boardDTO) {
        log.info("Create board : {}", boardDTO);
        return boardService.create(boardDTO);
    }

    @PatchMapping("")
    public ResponseEntity<?> updateBoard(@RequestBody BoardDTO boardDTO) {
        log.info("Update board : {}", boardDTO);
        return boardService.update(boardDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        log.info("Delete board : {}", id);
        return boardService.deleteBoard(id);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@RequestBody CommentsDTO commentsDTO) {
        log.info("Create comment : {}", commentsDTO);
        return commentsService.createComment(commentsDTO);
    }

    @PatchMapping("/comment")
    public ResponseEntity<?> updateComment(@RequestBody CommentsDTO commentsDTO) {
        log.info("Update board : {}", commentsDTO);
        return commentsService.updateComment(commentsDTO);
    }

    @GetMapping("/comment/{boardUuid}")
    public ResponseEntity<?> getComments(@PathVariable Long boardUuid) {
        log.info("Get comments : {}", boardUuid);
        return commentsService.getComments(boardUuid);
    }

    @DeleteMapping("/comment/{commentUuid}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentUuid) {
        log.info("Delete comment : {}", commentUuid);
        return commentsService.deleteComment(commentUuid);
    }

}