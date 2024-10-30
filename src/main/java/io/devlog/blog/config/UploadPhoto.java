package io.devlog.blog.config;

import io.devlog.blog.config.enums.ExceptionStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/photo")
public class UploadPhoto {

    @Value("${file.upload-dir}")
    private String fileDir;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> upload(MultipartFile file) {
        try {
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
