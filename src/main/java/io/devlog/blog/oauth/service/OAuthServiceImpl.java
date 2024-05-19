package io.devlog.blog.oauth.service;

import io.devlog.blog.oauth.DTO.OAuthDTO;
import io.devlog.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder pwEncoder;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.naver.client-authentication-method}")
    private String NAVER_CLIENT_AUTHENTICATION_METHOD;
    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String NAVER_AUTHORIZATION_GRANT_TYPE;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;
    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String NAVER_AUTHORIZATION_URI;
    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String NAVER_TOKEN_URI;
    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String NAVER_USER_INFO_URI;
    @Value("Bearer")
    private String tokenType;

    private String TYPE;
    private String CLIENT_ID;
    private String REDIRECT_URI;

    public OAuthDTO login(String code, String type) {
        TYPE = type;
        CLIENT_ID = NAVER_CLIENT_ID;
        REDIRECT_URI = NAVER_REDIRECT_URI;
        System.out.println("code : " + code);
        System.out.println("ENTRY");
        try {
            BufferedReader br = getBufferedReader(code);
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new OAuthDTO();
    }

    private BufferedReader getBufferedReader(String code) throws IOException {
        URL url = new URL(NAVER_USER_INFO_URI);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", tokenType + " " + code);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        int resCode = conn.getResponseCode();
        BufferedReader br;
        if (resCode == 200) {
            br = new BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
        } else {
            br = new BufferedReader(new java.io.InputStreamReader(conn.getErrorStream()));
        }
        return br;
    }

    public String getNaverLoginPage() {
        try {
            String state = new BigInteger(130, new SecureRandom()).toString();
            return NAVER_AUTHORIZATION_URI + "?response_type=code" + "&client_id=" + NAVER_CLIENT_ID + "&state=" + state + "&redirect_uri=" + NAVER_REDIRECT_URI;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("네이버 에러");
        }
    }

    public HashMap<String, String> getSocialTokens(String code) {
        String accessToken = "";
        String refreshToken = "";
        HashMap<String, String> social = new HashMap<>();
        social.put("tokenUri", NAVER_TOKEN_URI);
        social.put("authenticationMethod", NAVER_CLIENT_AUTHENTICATION_METHOD);
        social.put("grantType", NAVER_AUTHORIZATION_GRANT_TYPE);
        social.put("clientId", NAVER_CLIENT_ID);
        social.put("clientSecret", NAVER_CLIENT_SECRET);
        social.put("redirectUri", NAVER_REDIRECT_URI);
        social.put("state", new BigInteger(130, new SecureRandom()).toString());
        try {
            URL url = new URL(social.get("tokenUri"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(social.get("authenticationMethod"));
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=" + social.get("grantType") +
                    "client_id=" + social.get("clientId") +
                    "client_secret=" + social.get("clientSecret") +
                    "redirect_uri=" + social.get("redirectUri") +
                    "code=" + code +
                    "state=" + social.get("state");
            bw.write(sb);
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
