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
import io.devlog.blog.config.enums.ExceptionStatus;
import io.devlog.blog.security.Jwt.JwtService;
import io.devlog.blog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final CateRepository cateRepository;
    private final TagRepository tagRepository;
    private final BoardTagsRepository boardTagsRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final HttpServletRequest httpServletRequest;

    @Value("${file.upload-dir}")
    private String fileDir;

    public BoardServiceImpl(BoardRepository boardRepository, CateRepository cateRepository, TagRepository tagRepository, BoardTagsRepository boardTagsRepository, JwtService jwtService, UserRepository userRepository, HttpServletRequest httpServletRequest) {
        this.boardRepository = boardRepository;
        this.cateRepository = cateRepository;
        this.tagRepository = tagRepository;
        this.boardTagsRepository = boardTagsRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.httpServletRequest = httpServletRequest;
    }

    public Long checkJwt() {
        try {
            String accessToken = httpServletRequest.getHeader("Authorization");
            Long id = jwtService.getClaims(accessToken).get("id", Long.class);
            if (accessToken == null || id == 0 || !jwtService.validateToken(accessToken)) {
                return 0L;
            } else {
                // 2. accessToken이 있으나 expireDate가 지났으나 refreshToken이 만료 전 이면 accessToken 재발급 후 진행
                if (userRepository.existsByUserUuid(id)) {
                    return id;
                } else {
                    return 0L;
                }
            }
        } catch (Exception e) {
            return 0L;
        }

    }

    @Override
    public ResponseEntity<?> uploadPhoto(MultipartFile file) {
        try {
            if (file.getOriginalFilename() == null ||
                    file.getContentType() == null ||
                    !file.getContentType().startsWith("image/") ||
                    file.isEmpty()) {
                return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
            } else {
                String ogFileName = file.getOriginalFilename();
                String ext = ogFileName.substring(ogFileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString();
                String fileName = uuid + ext;
                file.transferTo(new File(fileDir + fileName));
                log.info("upload photo success : {}", fileName);
                return ResponseEntity.ok().body(fileName);

            }
        } catch (Exception e) {
            log.error("upload photo error", e);
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
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

    private ResponseEntity<?> checkLists(Slice<Board> lists) {
        if (!lists.isEmpty()) {
            if (lists.getContent().isEmpty())
                return ResponseEntity.noContent().build();
            else {
                log.info("lists : {}", lists);
                for (Board board : lists.getContent()) {
                    log.info("board : {}", board);
                }
                return ResponseEntity.ok(lists.get());
            }
        } else {
            return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<?> pagingBoards(int page, int size) {
        try {
            Pageable p = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardUuid"));
            Slice<Board> lists = boardRepository.findAll(p);
            return checkLists(lists);
        } catch (Exception e) {
            log.error("Server error : ", e);
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> pagingCateBoards(long cateUuid, int page, int size) {
        try {
            Pageable p = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardUuid"));
            Slice<Board> lists = boardRepository.findBoardByCategoriesCateUuid(cateUuid, p);
            return checkLists(lists);
        } catch (Exception e) {
            log.error("Server error : ", e);
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getCategories() {
        try {
            Long id = checkJwt();
            List<String> cateNames = cateRepository.findByUserCateName(id);
            return ResponseEntity.ok(cateNames);
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
            if (board.isPresent()) {
                BoardDTO boardDTO = BoardDTO.fromEntity(board.get());
                return ResponseEntity.ok().body(boardDTO);
            } else {
                return ResponseEntity.noContent().build();
            }
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
            Long id = checkJwt();
            if (id == 0) {
                return ResponseEntity.badRequest().body("Token is invalid");
            }
            Optional<Categories> category = cateRepository.findByCateName(boardDTO.getCategories());
            List<String> tagNames = boardDTO.getTags();
            List<Tags> recvtags = tagRepository.findByTagNameIn(tagNames);

            userRepository.findByUserUuid(id)
                    .ifPresent((user) -> {
                        boardDTO.setUserUuID(user.getUserUuid());
                        boardDTO.setUserName(user.getName());
                    });

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

    // 1.accessToken이 유효하거나 있으면 id 추출 해서 db에 있는지 체크 (create, update, delete)
    // 2. accessToken이 있으나 expireDate가 지났으나 refreshToken이 만료 전 이면 accessToken 재발급 후 진행 (create, update, delete)
    // 3. 작성자와 로그인한사람 일치 불일치 확인(update, delete)
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
            originalBoard.setBoardDate(LocalDateTime.now());

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