package io.devlog.blog.team.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/team")
public class TeamAPI {
    @GetMapping("/")
    public ResponseEntity<?> getTeamBlog() {
        return null;
    }

    @PostMapping("/{teamName}")
    public ResponseEntity<?> updateTeamBlog(@PathVariable String teamName) {
        return null;
    }
}
