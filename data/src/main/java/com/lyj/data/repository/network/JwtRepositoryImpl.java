package com.lyj.data.repository.network;


import com.lyj.domain.model.network.auth.JwtModel;
import com.lyj.domain.repository.network.JwtRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * id 파싱시 java Integer 클래스를 요구
 * Kotlin Class 에서 Java Class로 변경
 */
public class JwtRepositoryImpl implements JwtRepository {
    private static final byte[] SECRET_KEY = "pinstagramsecret".getBytes();

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public JwtModel parseJwt(String token){
        Claims claims = getClaims(token);
        return new JwtModel(
                claims.get("id",Integer.class),
                claims.get("name",String.class),
                claims.get("email",String.class)
        );
    }
}
