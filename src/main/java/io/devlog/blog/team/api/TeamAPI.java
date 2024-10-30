package io.devlog.blog.team.api;

import io.devlog.blog.team.dto.TBlogDTO;
import io.devlog.blog.team.service.TBlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/team")
public class TeamAPI {
    private final TBlogService tBlogService;

    public TeamAPI(TBlogService tBlogService) {
        this.tBlogService = tBlogService;
    }


    @GetMapping("")
    public ResponseEntity<?> getTeamBlog() {
        return tBlogService.getTeamBlog();
    }

    @GetMapping("/members")
    public ResponseEntity<?> getTeamBlogMembers() {
        return tBlogService.getTeamBlogMembers();
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyTeamBlog() {
        return tBlogService.getMyTeamBlog();
    }

    @PostMapping("")
    public ResponseEntity<?> createTeamBlog(@RequestBody TBlogDTO tBlogDTO) {
        return tBlogService.createTeamBlog(tBlogDTO);
    }

    @PatchMapping("")
    public ResponseEntity<?> updateTeamBlog(@RequestBody TBlogDTO tBlogDTO) {
        return tBlogService.updateTeamBlog(tBlogDTO);
    }

    @PostMapping("/{teamName}")
    public ResponseEntity<?> updateTeamBlog(@PathVariable String teamName) {
        return null;
    }
}
