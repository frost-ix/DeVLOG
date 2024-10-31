package io.devlog.blog.security.config;

import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class EmbeddedTomcatConfig implements WebServerFactoryCustomizer<TomcatReactiveWebServerFactory> {
    @Override
    public void customize(TomcatReactiveWebServerFactory factory) {
        factory.addConnectorCustomizers(connector ->
                connector.setAllowBackslash(true));
        factory.setUriEncoding(StandardCharsets.UTF_8);
    }
}