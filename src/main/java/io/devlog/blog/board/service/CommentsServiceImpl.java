package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.CommentsDTO;
import io.devlog.blog.board.entity.Comments;
import io.devlog.blog.board.repository.BoardRepository;
import io.devlog.blog.board.repository.CommentsRepository;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.repository.UserInfoRepository;
import io.devlog.blog.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CommentsServiceImpl implements CommentsService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CommentsRepository commentsRepository;
    private final BoardRepository boardRepository;
    private final UserInfoRepository userInfoRepository;

    public CommentsServiceImpl(JwtService jwtService, UserRepository userRepository, CommentsRepository commentsRepository, BoardRepository boardRepository, UserInfoRepository userInfoRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.commentsRepository = commentsRepository;
        this.boardRepository = boardRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public List<CommentsDTO> toDTO(List<Comments> commentsList) {
        return commentsList.stream()
                .map(Comments -> CommentsDTO.builder()
                        .commentUuid(Comments.getCommentUuid())
                        .comments(Comments.getComments())
                        .boardUuid(Comments.getBoard().getBoardUuid())
                        .userName(Comments.getUserName())
                        .commentDate(Comments.getCommentDate())
                        .userIcon(userInfoRepository.findByUserUuid(Comments.getUserUuid()).getUserIcon())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> getComments(Long boardUuid) {
        try {
            List<Comments> comments = commentsRepository.findByBoard_BoardUuid(boardUuid);
            List<CommentsDTO> commentsDTOS = toDTO(comments);
            return ResponseEntity.ok().body(commentsDTOS);
        } catch (Exception e) {
            log.error("Get comments error : {}", e.getMessage());
            return ResponseEntity.badRequest().body("Get comments error");
        }
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
                    .userUuid(id)
                    .userName(userRepository.findByUserName(id))
                    .board(boardRepository.findBoardByBoardUuid(commentsDTO.getBoardUuid()))
                    .commentDate(LocalDateTime.now())
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
            if (id == commentsRepository.findByUserUuid(commentsDTO.getCommentUuid())) {
                Comments comments = commentsRepository.findByUserUuidAndBoard_BoardUuid(id, commentsDTO.getBoardUuid(), commentsDTO.getCommentUuid());
                comments.setComments(commentsDTO.getComments());
                comments.setCommentDate(LocalDateTime.now());
                commentsRepository.save(comments);
                return ResponseEntity.ok().body("Update comment");
            }
            return ResponseEntity.badRequest().body("Update comment error");

        } catch (Exception e) {
            log.error("Update comment error : {}", e.getMessage());
            return ResponseEntity.badRequest().body("Update comment error");
        }
    }

    @Override
    public ResponseEntity<?> deleteComment(Long commentUuid) {
        try {
            Long id = jwtService.checkJwt();
            if (id == 0) {
                return ResponseEntity.badRequest().body("Token error");
            }
            if (id == commentsRepository.findByUserUuid(commentUuid)) {
                commentsRepository.deleteById(commentUuid);
                return ResponseEntity.ok().body("Delete comment");
            }
            return ResponseEntity.badRequest().body("Delete comment error");

        } catch (Exception e) {
            log.error("Delete comment error : {}", e.getMessage());
            return ResponseEntity.badRequest().body("Delete comment error");
        }
    }
}
