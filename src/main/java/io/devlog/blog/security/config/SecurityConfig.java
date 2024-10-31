package io.devlog.blog.security.config;

//import io.devlog.blog.security.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /***
     * Security Filter Chain
     * @param httpSecurity HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((req) ->
                        req
                                .requestMatchers("/", "/photo").permitAll()
                                .requestMatchers("/cate", "/cate/**", "/cate/pBlog/**", "/cate/tBlog/**").permitAll()
                                .requestMatchers("/mail", "/mail/**").permitAll()
                                .requestMatchers("/t", "/t/**").permitAll()
                                .requestMatchers("/oauth/**").permitAll()
                                .requestMatchers("/board", "/user").permitAll()
                                .requestMatchers("/p", "/p/**").permitAll()
                                .requestMatchers("/board/**", "/user/**").permitAll()
                                .requestMatchers("/board/**/**", "/user/**/**").permitAll()
                                .requestMatchers("/board/**/**/**", "/user/**/**/**").permitAll()
                                .requestMatchers("/oauth/**").permitAll()
                                .anyRequest().authenticated());
        return httpSecurity.build();
    }

    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        BCryptPasswordEncoder.BCryptVersion bCryptVersion = BCryptPasswordEncoder.BCryptVersion.$2Y;
        return new BCryptPasswordEncoder(bCryptVersion, 10);
    }

    /***
     * Password Encoder
     * @return Personal BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return bCryptPasswordEncoder();
    }
}