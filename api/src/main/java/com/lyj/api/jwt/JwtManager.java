package com.lyj.api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



/**
 * id 파싱시 java Integer 클래스를 요구
 * Kotlin Class 에서 Java Class로 변경
 */
public class JwtManager {
    private static final byte[] SECRET_KEY = "pinstagramsecret".getBytes();

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            return null;
        }
    }

    public JwtAuthData parseJwt(String token){
        Claims claims = getClaims(token);
        return new JwtAuthData(
                claims.get("id",Integer.class),
                claims.get("name",String.class),
                claims.get("email",String.class)
        );
    }
}
