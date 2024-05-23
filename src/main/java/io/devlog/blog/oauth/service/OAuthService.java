package io.devlog.blog.oauth.service;

import io.devlog.blog.oauth.DTO.info.GithubInfo;
import io.devlog.blog.oauth.DTO.info.GoogleInfo;
import io.devlog.blog.oauth.DTO.info.NaverInfo;

public interface OAuthService {
    NaverInfo loginNaver(String code, String state);

    GoogleInfo loginGoogle(String code, String state);

    GithubInfo loginGithub(String code, String state);
}
