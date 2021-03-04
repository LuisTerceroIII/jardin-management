package com.jardin.api.utilsFunctions;

import javax.servlet.http.HttpServletResponse;

import com.jardin.api.config.JwtTokenConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

public class JwtTokenUtils {

  public JwtTokenUtils() {}

  public static boolean isTokenValid(HttpServletResponse res) {
    String isTokenValidString = res.getHeader("isTokenValid");
    return Boolean.parseBoolean(isTokenValidString);
  }

  public static String generateJwtToken() {
    byte[] secretKey = JwtTokenConfig.getJwsKey().getBytes();
    return Jwts
            .builder()
            .setSubject(JwtTokenConfig.getSubject())
            .setIssuedAt(new Date())
            .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
            .signWith(Keys.hmacShaKeyFor(secretKey))
            .compact();
  }
}
