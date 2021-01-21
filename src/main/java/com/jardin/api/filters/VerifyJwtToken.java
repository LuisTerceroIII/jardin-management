package com.jardin.api.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class VerifyJwtToken implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String secretKey = "asegurandoJardinAPI2021LuisHECTOResPINOZAnaVaRRete9488888";

        HttpServletRequest req = (HttpServletRequest) request;
        String sessionToken = req.getHeader("sessionToken");
        HttpServletResponse res = (HttpServletResponse) response;

        if (sessionToken != null) {
            try {
                Jws<Claims> JwtBody = Jwts.parserBuilder() //Si falla este parseo, va a JwtException, puede ir ahi si el token ha expirado por ejemplo.
                        .setSigningKey(secretKey.getBytes())
                        .build()
                        .parseClaimsJws(sessionToken);
                if (JwtBody.getBody().getSubject().equals("Jardin")) {
                    res.setHeader("isTokenValid", "true");
                } else {
                    res.setHeader("isTokenValid", "false");
                }
            } catch (JwtException e) {
                throw new RuntimeException(e);
            }
        } else {
            res.setHeader("isTokenValid", "false");
        }
        chain.doFilter(request, res);
    }

    @Override
    public void destroy() {

    }
}
