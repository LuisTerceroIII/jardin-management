package com.jardin.api.filters;

import com.jardin.api.config.JwtTokenConfig;
import io.jsonwebtoken.*;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class VerifyJwtToken extends OncePerRequestFilter {

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return new AntPathMatcher()
    .match("/management/jardin-api/v1/login", request.getServletPath());
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest req,
    HttpServletResponse res,
    FilterChain chain
  ) throws ServletException, IOException {
    byte[] secretKey = JwtTokenConfig.getJwsKey().getBytes();
    String subject = JwtTokenConfig.getSubject();
    String sessionToken = req.getHeader("sessionToken");
    if (sessionToken != null) {
      try {
        Jws<Claims> jwtBody = Jwts
          .parserBuilder() //Si falla este parseo, va a JwtException, puede ir ahi si el token ha expirado por ejemplo.
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(sessionToken);
        if (jwtBody.getBody().getSubject().equals(subject)) {
          res.setHeader("isTokenValid", "true");
        } else {
          res.setHeader("isTokenValid", "false");
        }
      } catch (ExpiredJwtException expiredJwtException) {
        System.out.println("Sesion vencida!!!");
        System.out.println("token session expired");
        res.setHeader("isTokenValid", "false");
      } catch (JwtException e) {
        System.out.println(e.getMessage());
        System.out.println("Dentro de TOKEN NO VALIDO !!!!!!!!");
      }
    } else {
      res.setHeader("isTokenValid", "false");
    }
    chain.doFilter(req, res);
  }
}
