package io.devlog.blog.team.service;

import io.devlog.blog.team.dto.TBlogDTO;
import org.springframework.http.ResponseEntity;

public interface TBlogService {
    ResponseEntity<?> getTeamBlog();

    ResponseEntity<?> getTeamBlogMembers(TBlogDTO tBlogDTO);

    ResponseEntity<?> getMyTeamBlog();

    ResponseEntity<?> createTeamBlog(TBlogDTO tBlogDTO);

    ResponseEntity<?> updateTeamBlog(TBlogDTO tBlogDTO);

    ResponseEntity<?> deleteTeamBlog();
}
