package com.jardin.api.config;

public class JwtTokenConfig {
    private static final String subject = "Jardin";
    private static final String JwtKey = "asegurandoJardinAPI2021LuisHECTOResPINOZAnaVaRRete9488888";
    public static String getSubject() { return subject; }
    public static String getJwsKey() { return JwtKey; }

}
