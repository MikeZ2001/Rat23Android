package com.example.ratatouille23client.amplify.auth;

public interface SignInCallback extends AuthCallback{
    @Override
    void success();

    void needNewPassword();

    @Override
    void error(Throwable error);
}
