package io.devlog.blog.pblog.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ResourceLoader;
import java.nio.file.Files;

@Log4j2
@RestController
@RequestMapping("/pblog")
public class PblogAPI {

    private ResourceLoader resourceLoader;
    @GetMapping("/")
    public ResponseEntity<String> index() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:static/index.html");
        String htmlContent = Files.readString(resource.getFile().toPath());
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlContent);
    }

}
