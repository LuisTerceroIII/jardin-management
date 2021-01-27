package com.jardin.api.services;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class TokenVerify {

    public TokenVerify() {
    }

    public static  boolean isTokenValid(HttpServletResponse res) {
        String isTokenValidString = res.getHeader("isTokenValid");
        return Boolean.parseBoolean(isTokenValidString);
    }
}
