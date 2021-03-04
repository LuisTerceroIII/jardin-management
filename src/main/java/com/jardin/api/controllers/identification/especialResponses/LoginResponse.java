package com.jardin.api.controllers.identification.especialResponses;

public class LoginResponse {

  private boolean validCredentials;
  private String sessionToken;

  public LoginResponse() {}

  public LoginResponse(boolean validCredentials, String sessionToken) {
    this.validCredentials = validCredentials;
    this.sessionToken = sessionToken;
  }

  public boolean isValidCredentials() {
    return validCredentials;
  }

  public String getSessionToken() {
    return sessionToken;
  }

  @Override
  public String toString() {
    return (
      "LoginResponse{" +
      "validCredentials=" +
      validCredentials +
      ", sessionToken='" +
      sessionToken +
      '\'' +
      '}'
    );
  }
}
