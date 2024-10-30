package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.CommentsDTO;
import org.springframework.http.ResponseEntity;

public interface CommentsService {

    ResponseEntity<?> getComments(Long boardUuid);

    ResponseEntity<?> createComment(CommentsDTO commentsDTO);

    ResponseEntity<?> updateComment(CommentsDTO commentsDTO);

    ResponseEntity<?> deleteComment(Long commentUuid);

}
