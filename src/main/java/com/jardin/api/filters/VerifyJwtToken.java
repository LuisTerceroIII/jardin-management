package com.jardin.api.filters;

import io.jsonwebtoken.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class VerifyJwtToken extends OncePerRequestFilter {
/*
    @Bean
    public FilterRegistrationBean<VerifyJwtToken> loggingFilter(){
        FilterRegistrationBean<VerifyJwtToken> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(new VerifyJwtToken());
        registrationBean.addUrlPatterns("management/jardin-api/v1/garment/*");

        return registrationBean;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String secretKey = "asegurandoJardinAPI2021LuisHECTOResPINOZAnaVaRRete9488888";
        System.out.println("DEEEENTRO  PAPAPAPAPAPAP");
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
            } catch (ExpiredJwtException expiredJwtException) {
                System.out.println("Sesion vencida!!!");
                System.out.println("token session expired");
                res.setHeader("isTokenValid", "false");
            } catch (JwtException e) {
                System.out.println("Dentro de TOKEN NO VALIDO !!!!!!!!");
            }
        } else {
            res.setHeader("isTokenValid", "false");
        }
        chain.doFilter(request, res);
    }

    @Override
    public void destroy() {

    }*/
@Override
protected boolean shouldNotFilter(HttpServletRequest request)  throws ServletException {
    return new AntPathMatcher().match("/management/jardin-api/v1/loginToken", request.getServletPath());
}

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {

        String secretKey = "asegurandoJardinAPI2021LuisHECTOResPINOZAnaVaRRete9488888";
        String sessionToken = req.getHeader("sessionToken");
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
            } catch (ExpiredJwtException expiredJwtException) {
                System.out.println("Sesion vencida!!!");
                System.out.println("token session expired");
                res.setHeader("isTokenValid", "false");
            } catch (JwtException e) {
                System.out.println("Dentro de TOKEN NO VALIDO !!!!!!!!");
            }
        } else {
            res.setHeader("isTokenValid", "false");
        }
        chain.doFilter(req, res);
    }


}
