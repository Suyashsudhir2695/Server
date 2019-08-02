package com.example.change.foodorderserver.Model;

public class Token {
    private boolean isServerToken;
    private String token;

    public Token(String token, boolean isServerToken) {
        this.isServerToken = isServerToken;
        this.token = token;
    }

    public Token() {
    }

    public boolean isServerToken() {
        return isServerToken;
    }

    public void setServerToken(boolean serverToken) {
        isServerToken = serverToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
