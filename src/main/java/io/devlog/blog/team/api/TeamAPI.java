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

    @PostMapping("")
    public ResponseEntity<?> createTeamBlog(TBlogDTO tBlogDTO) {
        return tBlogService.createTeamBlog(tBlogDTO);
    }

    @PostMapping("/{teamName}")
    public ResponseEntity<?> updateTeamBlog(@PathVariable String teamName) {
        return null;
    }
}
