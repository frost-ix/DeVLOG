package io.devlog.blog.config;

import io.devlog.blog.config.enums.ExceptionStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/photo")
@Log4j2
public class UploadPhoto {
    @Value("${file.upload-dir}")
    private String fileDir;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> upload(@RequestParam(value = "image", required = false) MultipartFile file) {
        try {
            log.info("Upload photo");
            if (file.getOriginalFilename() == null ||
                    file.getContentType() == null ||
                    !file.getContentType().startsWith("image/") ||
                    file.isEmpty()) {
                return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
            } else {
                String ogFileName = file.getOriginalFilename();
                String ext = Objects.requireNonNull(ogFileName).substring(ogFileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString();
                String fileName = uuid + ext;
                file.transferTo(new File(fileDir + fileName));
                return ResponseEntity.ok(fileName);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionStatus.BAD_REQUEST);
        }
    }
}
