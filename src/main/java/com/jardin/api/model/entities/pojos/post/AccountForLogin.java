package com.jardin.api.model.entities.pojos.post;

public class AccountForLogin {

    private String name;
    private String password;

    public AccountForLogin(String name, String password) {
        this.name = name;
        this.password = password;
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

    @Override
    public String toString() {
        return "AccountForLogin{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
