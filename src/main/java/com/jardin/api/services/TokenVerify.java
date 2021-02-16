package com.jardin.api.services;

import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class TokenVerify {

  public TokenVerify() {}

  public static boolean isTokenValid(HttpServletResponse res) {
    String isTokenValidString = res.getHeader("isTokenValid");
    return Boolean.parseBoolean(isTokenValidString);
  }
}
