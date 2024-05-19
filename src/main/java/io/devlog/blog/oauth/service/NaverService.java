package io.devlog.blog.oauth.service;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Service
public class NaverService {
    private final String redirectURI = URLEncoder.encode("", StandardCharsets.UTF_8);

    public String createURL() throws UnsupportedEncodingException {
        SecureRandom secureRandom = new SecureRandom();
        String state = new BigInteger(130, secureRandom).toString();
        String url = "https://nid.naver.com/oauth2.0/authorize?" +
                "response_type=code" +
                "&state=" + state +
                "&redirect_uri=" + redirectURI;
        return url;
    }
}
