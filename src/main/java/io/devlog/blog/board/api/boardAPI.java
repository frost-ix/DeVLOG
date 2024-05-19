package io.devlog.blog.board.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(value = "/")
public class boardAPI {
    @GetMapping(value = "/test", produces = "application/json")
    public String test() {
        return "test";
    }

    @GetMapping()
    public String index(@RequestParam String code, @RequestParam String state) {
        log.info("code : {}, state : {}", code, state);
        return "code : " + code + "\n state : " + state;
    }
}
