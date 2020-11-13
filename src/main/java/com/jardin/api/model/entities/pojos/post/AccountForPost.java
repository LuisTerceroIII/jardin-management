package com.jardin.api.model.entities.pojos.post;

public class AccountForPost {

    private String name;
    private String password;
    private int secretCode;
    private Long privileges;

   public AccountForPost(String name, String password, int secretCode) {
        this.name = name;
        this.password = password;
        this.secretCode = secretCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(int secretCode) {
        this.secretCode = secretCode;
    }

    public Long getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Long privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return "AccountForPost{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", secretCode=" + secretCode +
                ", privileges=" + privileges +
                '}';
    }
}
