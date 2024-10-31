package io.devlog.blog.pblog.service;

import io.devlog.blog.pblog.DTO.PblogDTO;
import org.springframework.http.ResponseEntity;

public interface PblogService {
    ResponseEntity<?> getPblog();

    ResponseEntity<?> getPblog(String domain);

    ResponseEntity<?> update(PblogDTO pblogDTO);

    ResponseEntity<?> updateName(PblogDTO pblogDTO);

    ResponseEntity<?> updateDomain(PblogDTO pblogDTO);

    ResponseEntity<?> updateBanner(PblogDTO pblogDTO);
}
