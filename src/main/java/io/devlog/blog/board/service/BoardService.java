package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.BoardDTO;
import org.springframework.http.ResponseEntity;

public interface BoardService {
    ResponseEntity<?> getBoards();

    ResponseEntity<?> pagingBoards(int page, int size);

    ResponseEntity<?> pagingCateBoards(long cateUuid, int page, int size);

    ResponseEntity<?> getUserBoards();

    ResponseEntity<?> getPDomainBoardList(String domain);

    ResponseEntity<?> getTDomainBoardList(String domain);

    ResponseEntity<?> getBoard(Long id);

    ResponseEntity<?> getCateBoardList(String pDomain, String cateName);

    ResponseEntity<?> getTCateBoardList(String tDomain, String cateName);

    ResponseEntity<?> getTagBoardList(String tagName);

    ResponseEntity<?> create(BoardDTO boardDTO);

    ResponseEntity<?> update(BoardDTO boardDTO);

    ResponseEntity<?> deleteBoard(Long id);

    ResponseEntity<?> deleteAll();

    boolean exists(String id);

    long count();
}