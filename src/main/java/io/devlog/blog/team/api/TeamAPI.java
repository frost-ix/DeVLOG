package io.devlog.blog.team.api;

import io.devlog.blog.team.dto.TBlogDTO;
import io.devlog.blog.team.service.TBlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/t")
public class TeamAPI {
    private final TBlogService tBlogService;

    public TeamAPI(TBlogService tBlogService) {
        this.tBlogService = tBlogService;
    }


    @GetMapping("")
    public ResponseEntity<?> getTeamBlog() {
        return tBlogService.getTeamBlog();
    }

    @GetMapping("/{tDomain}")
    public ResponseEntity<?> getTeamBlog(@PathVariable String tDomain) {
        return tBlogService.getTeamBlog(tDomain);
    }

    @GetMapping("/{tDomain}/members")
    public ResponseEntity<?> getTeamBlogMembers(@PathVariable String tDomain) {
        return tBlogService.getTeamBlogMembers(tDomain);
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

    @PatchMapping("/domain")
    public ResponseEntity<?> updateTeamBlogDomain(@RequestBody TBlogDTO tBlogDTO) {
        return tBlogService.updateTeamBlogDomain(tBlogDTO);
    }

    @PatchMapping("/title")
    public ResponseEntity<?> updateTeamBlogTitle(@RequestBody TBlogDTO tBlogDTO) {
        return tBlogService.updateTeamBlogTitle(tBlogDTO);
    }

    @PatchMapping("/banner")
    public ResponseEntity<?> updateTeamBlogBanner(@RequestBody TBlogDTO tBlogDTO) {
        return tBlogService.updateTeamBlogBanner(tBlogDTO);
    }

    @PatchMapping("/info")
    public ResponseEntity<?> updateTeamBlogInfo(@RequestBody TBlogDTO tBlogDTO) {
        return tBlogService.updateTeamBlogInfo(tBlogDTO);
    }

    @PatchMapping("/name")
    public ResponseEntity<?> updateTeamBlogName(@RequestBody TBlogDTO tBlogDTO) {
        return tBlogService.updateTeamBlogName(tBlogDTO);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteTeamBlog() {
        return tBlogService.deleteTeamBlog();
    }
}
