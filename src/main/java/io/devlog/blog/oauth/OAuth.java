package io.devlog.blog.oauth;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

public class OAuth implements ApplicationListener<ApplicationEvent> {
    private final Environment env;

    public OAuth(Environment env) {
        this.env = env;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("OAuth : " + env.getProperty("spring.profiles.active"));
    }
}