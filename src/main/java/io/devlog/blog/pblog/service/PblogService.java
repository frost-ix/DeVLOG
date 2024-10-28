package io.devlog.blog.pblog.service;


import io.devlog.blog.pblog.DTO.PblogDTO;
import org.springframework.http.ResponseEntity;

public interface PblogService {
    ResponseEntity<?> getPblog();

    ResponseEntity<?> update(PblogDTO pblogDTO);
}
