package com.example.ratatouille23client.amplify.auth;

public interface SignOutCallback {
    void success();

    void partialSignOut();

    void failedSignOut();
}
