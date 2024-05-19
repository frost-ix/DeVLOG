package io.devlog.blog.security.config;

import io.devlog.blog.oauth.service.CustomOAuth2UserService;
import io.devlog.blog.user.enums.AccessRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    /***
     * Security Filter Chain
     * @param httpSecurity HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((req) ->
                        req
                                .requestMatchers("/", "/index.html", "/board/**").permitAll()
                                .requestMatchers("/api/v1/**").hasRole(AccessRole.CLIENT.getRole())
                                .anyRequest().authenticated())
                .oauth2Login(oauth2 -> {
                    oauth2.userInfoEndpoint(userInfo -> {
                        userInfo.userService(customOAuth2UserService);
                    });
                });
        httpSecurity.logout((auth) -> auth.logoutUrl("/logout").logoutSuccessUrl("/index.html"));
        return httpSecurity.build();
    }

    /***
     * bcrypt password encoder
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        BCryptPasswordEncoder.BCryptVersion bCryptVersion = BCryptPasswordEncoder.BCryptVersion.$2Y;
        return new BCryptPasswordEncoder(bCryptVersion, 10);
    }
}