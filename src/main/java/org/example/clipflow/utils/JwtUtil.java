package org.example.clipflow.utils;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.example.clipflow.exception.BaseException;
import org.springframework.util.ObjectUtils;

import java.util.Date;

public class JwtUtil {
    private static final long EXPIRE = 60 * 60 * 1000 * 24; //token过期时间，System.currentTimeMillis()为毫秒，1小时为60*60*1000
    private static final String SECRET_KEY = "imitate-lucky-journey";
    public static String getJwtToken(long id, String nickName) {

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("guli-user")

                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))

                .claim("id", id)
                .claim("nickName", nickName)

                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static boolean isEmpty(HttpServletRequest request) {
        try {
            if (ObjectUtils.isEmpty(request.getHeader("Token"))) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static Long getUserId(HttpServletRequest request) {
        String jwt = request.getHeader("token");
        if (ObjectUtils.isEmpty(jwt)) {
            return null;
        }
        try {
            Jws<Claims> claimsJws = Jwts
                    .parser()
                    .setSigningKey(SECRET_KEY) // 设置签名密钥
                    .parseClaimsJws(jwt); // 解析 JWT
            Claims jwsBody = claimsJws.getBody();
            return Long.valueOf(jwsBody.get("id").toString());
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new BaseException("JWT签名认证失败");
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new BaseException("JWT已过期");
        } catch (JwtException e) {
            e.printStackTrace();
            throw new BaseException("JWT解析异常");
        }
    }
}
