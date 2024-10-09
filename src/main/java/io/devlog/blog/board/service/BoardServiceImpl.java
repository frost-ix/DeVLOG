package io.devlog.blog.board.service;

import io.devlog.blog.board.DTO.BoardDTO;
import io.devlog.blog.board.entity.Board;
import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.board.entity.Tags;
import io.devlog.blog.board.repository.BoardRepository;
import io.devlog.blog.board.repository.CateRepository;
import io.devlog.blog.board.repository.TagRepository;
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

    public BoardServiceImpl(BoardRepository boardRepository, CateRepository cateRepository, TagRepository tagRepository) {
        this.boardRepository = boardRepository;
        this.cateRepository = cateRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public ResponseEntity<?> getBoards() {
        try {
            Optional<List<Board>> boards = Optional.of(boardRepository.findBoardBy());
            Optional<List<BoardDTO>> boardDTOS = boards.map(boardList -> boardList.stream()
                    .map(board -> BoardDTO.builder()
                            .boardUuid(board.getBoardUuid())
                            .categories(board.getCategories().getCateName())
                            .title(board.getBoardTitle())
                            .content(board.getBoardContent())
                            .userUuID(board.getUserUuid())
                            .build())
                    .collect(Collectors.toList()));
            return ResponseEntity.ok(boardDTOS);

        } catch (Exception e) {
            log.error("get boards error", e);
            return ResponseEntity.badRequest().body("get boards error");
        }
    }

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
    public Optional<Board> getBoard(Long id) {
        try {
            log.info("get board by id: {}", id);
            return boardRepository.findOneByBoardUuid(id);
        } catch (Exception e) {
            log.error("get board by id error", e);
            return Optional.empty();
        }
    }

    @Override
    public Board create(BoardDTO boardDTO) {
        Categories category = cateRepository.findByCateName(boardDTO.getCategories())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
        List<String> tagNames = boardDTO.getTags();
        List<Tags> recvtags = tagRepository.findByTagNameIn(tagNames);

        List<Tags> newTags = tagNames.stream()
                .filter(tagName -> recvtags.stream()
                        .noneMatch(existingTag -> existingTag.getTagName().equals(tagName)))
                .map(tagName -> new Tags(null, null, tagName))
                .collect(Collectors.toList());

        if (!newTags.isEmpty()) {
            tagRepository.saveAll(newTags);
        }
        recvtags.addAll(newTags);
        Board board = boardDTO.toEntity(category, recvtags);
        return boardRepository.save(board);
    }


    @Override
    public Board update(BoardDTO boardDTO) {
        Board originalBoard = boardRepository.findOneByBoardUuid(boardDTO.getBoardUuid())
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        Categories category = cateRepository.findByCateName(boardDTO.getCategories())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        List<String> tagNames = boardDTO.getTags();
        List<Tags> recvtags = tagRepository.findByTagNameIn(tagNames);

        List<Tags> newTags = tagNames.stream()
                .filter(tagName -> recvtags.stream()
                        .noneMatch(existingTag -> existingTag.getTagName().equals(tagName)))
                .map(tagName -> new Tags(null, originalBoard, tagName))
                .collect(Collectors.toList());

        if (!newTags.isEmpty()) {
            tagRepository.saveAll(newTags);
        }

        recvtags.addAll(newTags);

        originalBoard.setBoardTitle(boardDTO.getTitle());
        originalBoard.setBoardContent(boardDTO.getContent());
        originalBoard.setCategories(category);
        originalBoard.setTags(recvtags); // 태그 리스트 업데이트

        // 8. 수정된 게시물 저장 및 반환
        return boardRepository.save(originalBoard);
    }


    @Override
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
        System.out.println("delete board by id:"+id);
    }

    @Override
    public void deleteAll() {
        boardRepository.deleteAll();
        log.info("delete all boards");
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