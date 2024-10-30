package io.devlog.blog.pblog.api;

import io.devlog.blog.pblog.DTO.PblogDTO;
import io.devlog.blog.pblog.service.PblogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/p")
public class PblogAPI {
    private final PblogService pblogService;

    public PblogAPI(PblogService pblogService) {
        this.pblogService = pblogService;
    }

    @GetMapping("")
    public ResponseEntity<?> getPblog() {
        log.info("Get pblog");
        return pblogService.getPblog();
    }

    @PatchMapping("")
    public ResponseEntity<?> updatePblog(@RequestBody PblogDTO pblogDTO) {
        return pblogService.update(pblogDTO);
    }
}
