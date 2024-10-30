package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.CommentsDTO;
import io.devlog.blog.board.entity.Comments;
import io.devlog.blog.board.repository.BoardRepository;
import io.devlog.blog.board.repository.CommentsRepository;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CommentsServiceImpl implements CommentsService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CommentsRepository commentsRepository;
    private final BoardRepository boardRepository;

    public CommentsServiceImpl(JwtService jwtService, UserRepository userRepository, CommentsRepository commentsRepository, BoardRepository boardRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.commentsRepository = commentsRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    public ResponseEntity<?> getComments(Long boardUuid) {
        return null;
    }

    @Override
    public ResponseEntity<?> createComment(CommentsDTO commentsDTO) {
        try {
            Long id = jwtService.checkJwt();
            if (id == 0) {
                return ResponseEntity.badRequest().body("Token error");
            }
            commentsDTO.setUserUuid(id);
            commentsDTO.setUserName(userRepository.findByUserName(id));
            Comments comments = Comments.builder()
                    .comments(commentsDTO.getComments())
                    .imagePath(commentsDTO.getImagePath())
                    .userUuid(id)
                    .userName(userRepository.findByUserName(id))
                    .board(boardRepository.findBoardByBoardUuid(commentsDTO.getBoardUuid()))
                    .build();
            commentsRepository.save(comments);
            return ResponseEntity.ok().body("Create comment");

        } catch (Exception e) {
            log.error("Create comment error : {}", e.getMessage());
            return ResponseEntity.badRequest().body("Create comment error");
        }
    }

    @Override
    public ResponseEntity<?> updateComment(CommentsDTO commentsDTO) {
        try {
            Long id = jwtService.checkJwt();
            if (id == 0) {
                return ResponseEntity.badRequest().body("Token error");
            }
            return ResponseEntity.ok().body("Update comment");
        } catch (Exception e) {
            log.error("Update comment error : {}", e.getMessage());
            return ResponseEntity.badRequest().body("Update comment error");
        }
    }
}
