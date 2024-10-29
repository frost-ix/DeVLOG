package io.devlog.blog.team.service;

import io.devlog.blog.team.dto.TBlogDTO;
import org.springframework.http.ResponseEntity;

public interface TBlogService {
    ResponseEntity<?> getTeamBlog();

    ResponseEntity<?> getTeamBlogMembers();

    ResponseEntity<?> createTeamBlog(TBlogDTO tBlogDTO);
}
