package io.devlog.blog.team.service;

import io.devlog.blog.team.dto.TBlogDTO;
import org.springframework.http.ResponseEntity;

public interface TBlogService {
    ResponseEntity<?> getTeamBlog();

    ResponseEntity<?> getTeamBlog(String tDomain);

    ResponseEntity<?> getTeamBlogMembers(TBlogDTO tBlogDTO);

    ResponseEntity<?> getMyTeamBlog();

    ResponseEntity<?> createTeamBlog(TBlogDTO tBlogDTO);

    ResponseEntity<?> updateTeamBlog(TBlogDTO tBlogDTO);

    ResponseEntity<?> updateTeamBlogDomain(TBlogDTO tBlogDTO);

    ResponseEntity<?> updateTeamBlogTitle(TBlogDTO tBlogDTO);

    ResponseEntity<?> updateTeamBlogBanner(TBlogDTO tBlogDTO);

    ResponseEntity<?> updateTeamBlogInfo(TBlogDTO tBlogDTO);

    ResponseEntity<?> updateTeamBlogName(TBlogDTO tBlogDTO);

    ResponseEntity<?> deleteTeamBlog();
}
