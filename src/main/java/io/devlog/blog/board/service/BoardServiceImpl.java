package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.BoardDTO;
import io.devlog.blog.board.entity.Board;
import io.devlog.blog.board.entity.BoardTags;
import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.board.entity.Tags;
import io.devlog.blog.board.repository.BoardRepository;
import io.devlog.blog.board.repository.BoardTagsRepository;
import io.devlog.blog.board.repository.CateRepository;
import io.devlog.blog.board.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final CateRepository cateRepository;
    private final TagRepository tagRepository;
    private final BoardTagsRepository boardTagsRepository;

    public BoardServiceImpl(BoardRepository boardRepository, CateRepository cateRepository, TagRepository tagRepository, BoardTagsRepository boardTagsRepository) {
        this.boardRepository = boardRepository;
        this.cateRepository = cateRepository;
        this.tagRepository = tagRepository;
        this.boardTagsRepository = boardTagsRepository;
    }

    private List<BoardDTO> streamBoards(List<Board> boardList) {
        return boardList.stream()
                .map(board -> BoardDTO.builder()
                        .boardUuid(board.getBoardUuid())
                        .categories(board.getCategories().getCateName())
                        .title(board.getBoardTitle())
                        .content(board.getBoardContent())
                        .userUuID(board.getUserUuid())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> getBoards() {
        try {
            Optional<List<Board>> boards = Optional.of(boardRepository.findBoardBy());
            Optional<List<BoardDTO>> boardDTOS = streamBoards(boards.orElseThrow()).stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of));
            return ResponseEntity.ok(boardDTOS);

        } catch (Exception e) {
            log.error("get boards error", e);
            return ResponseEntity.badRequest().body("get boards error");
        }
    }

//    @Override
//    public ResponseEntity<?> pagingBoards(int start, int size) {
//        try {
//            Pageable pageable = (Pageable) PageRequest.of(start, size);
//            Slice<Board> lists = boardRepository.findAll(pageable);
//            if (!lists.isEmpty()) {
//                if (lists.getContent().isEmpty())
//                    return ResponseEntity.noContent().build();
//                else {
//                    return ResponseEntity.ok(lists.get());
//                }
//            } else {
//                return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
//            }
//        } catch (Exception e) {
//            log.error("Server error : ", e);
//            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
//        }
//    }

    @Override
    public ResponseEntity<?> getCategories() {
        try {
            Optional<List<Categories>> categories = Optional.of(cateRepository.findAll());
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            log.error("get categories error", e);
            return ResponseEntity.badRequest().body("get categories error");
        }
    }

    @Override
    public ResponseEntity<?> getUserBoards(Long id) {
        try {
            Optional<List<Board>> boards = boardRepository.findBoardByUserUuid(id);
            if (boards.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                Optional<List<BoardDTO>> boardDTOS = streamBoards(boards.orElseThrow()).stream()
                        .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of));
                return ResponseEntity.ok(boardDTOS);
            }
        } catch (Exception e) {
            log.error("Error of get User boards", e);
            return ResponseEntity.badRequest().body("Error of get User boards");
        }
    }

    @Override
    public ResponseEntity<?> getBoard(Long id) {
        try {
            log.info("get board by id: {}", id);
            Optional<Board> board = boardRepository.findOneByBoardUuid(id);
            return ResponseEntity.ok().body(board);
        } catch (Exception e) {
            log.error("get board by id error", e);
            return ResponseEntity.badRequest().body("get board by id error");
        }
    }

    private List<Tags> streamTags(List<String> tagNames, List<Tags> recvtags) {
        return tagNames.stream()
                .filter(tagName -> recvtags.stream()
                        .noneMatch(existingTag -> existingTag.getTagName().equals(tagName)))
                .map(tagName -> new Tags(null, tagName, null))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> create(BoardDTO boardDTO) {
        try {
            Optional<Categories> category = cateRepository.findByCateName(boardDTO.getCategories());
            List<String> tagNames = boardDTO.getTags();
            List<Tags> recvtags = tagRepository.findByTagNameIn(tagNames);

            List<Tags> newTags = streamTags(tagNames, recvtags);

            if (!newTags.isEmpty()) {
                tagRepository.saveAll(newTags);
            }
            recvtags.addAll(newTags);

            Board board = boardDTO.toEntity(category.orElse(null), null);
            boardRepository.save(board);

            List<BoardTags> boardTags = recvtags.stream()
                    .map(tag -> new BoardTags(null, board, tag))
                    .collect(Collectors.toList());

            boardTagsRepository.saveAll(boardTags);

            return ResponseEntity.status(200).body("create board success");
        } catch (Exception e) {
            log.error("create board error", e);
            return ResponseEntity.badRequest().body("create board error");
        }
    }

    @Override
    public ResponseEntity<?> update(BoardDTO boardDTO) {
        try {
            Board originalBoard = boardRepository.findOneByBoardUuid(boardDTO.getBoardUuid())
                    .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
            Categories category = cateRepository.findByCateName(boardDTO.getCategories())
                    .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

            List<String> tagNames = boardDTO.getTags();
            List<Tags> recvtags = tagRepository.findByTagNameIn(tagNames);

            List<Tags> newTags = streamTags(tagNames, recvtags);

            if (!newTags.isEmpty()) {
                tagRepository.saveAll(newTags);
            }

            recvtags.addAll(newTags);

            originalBoard.setBoardTitle(boardDTO.getTitle());
            originalBoard.setBoardContent(boardDTO.getContent());
            originalBoard.setCategories(category);

            boardRepository.save(originalBoard);
            return ResponseEntity.status(200).body("update board success");
        } catch (Exception e) {
            log.error("update board error", e);
            return ResponseEntity.badRequest().body("update board error");
        }
    }


    @Override
    @Transactional
    public ResponseEntity<?> deleteBoard(Long id) {
        try {
            Board board = boardRepository.findOneByBoardUuid(id)
                    .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
            boardTagsRepository.deleteByBoard(board);
            boardRepository.deleteById(id);
            return ResponseEntity.status(200).body("200");
        } catch (Exception e) {
            log.error("delete board by id error", e);
            return ResponseEntity.badRequest().body("delete board by id error");
        }
    }

    @Override
    public ResponseEntity<?> deleteAll() {
        try {
            boardRepository.deleteAll();
            return ResponseEntity.status(200).body("200");
        } catch (Exception e) {
            log.error("delete all boards error", e);
            return ResponseEntity.badRequest().body("delete all boards error");
        }
    }

    @Override
    public boolean exists(String id) {
        return boardRepository.existsById(id);
    }

    @Override
    public long count() {
        return boardRepository.count();
    }
}