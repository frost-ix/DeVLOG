package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.BoardDTO;
import io.devlog.blog.board.DTO.CateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
    ResponseEntity<?> uploadPhoto(MultipartFile file);

    ResponseEntity<?> getBoards();


    ResponseEntity<?> pagingBoards(int page, int size);

    ResponseEntity<?> pagingCateBoards(long cateUuid, int page, int size);

    ResponseEntity<?> getUserBoards();

    ResponseEntity<?> getBoard(Long id);

    ResponseEntity<?> getCateBoardList(CateDTO cateDTO);

    ResponseEntity<?> getTagBoardList(String tagName);

    ResponseEntity<?> create(BoardDTO boardDTO);

    ResponseEntity<?> update(BoardDTO boardDTO);

    ResponseEntity<?> deleteBoard(Long id);

    ResponseEntity<?> deleteAll();

    boolean exists(String id);

    long count();
}