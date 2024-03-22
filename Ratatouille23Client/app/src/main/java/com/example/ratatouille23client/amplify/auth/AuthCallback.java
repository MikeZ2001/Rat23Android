package com.example.ratatouille23client.amplify.auth;

public interface AuthCallback {
    void success();
    void error(Throwable error);
}
