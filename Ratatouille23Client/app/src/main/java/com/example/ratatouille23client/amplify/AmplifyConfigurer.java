package com.example.ratatouille23client.amplify;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;

public class AmplifyConfigurer extends Application {
    public void onCreate() {
        super.onCreate();

       try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
           Amplify.configure(getApplicationContext());
        } catch (AmplifyException error) {

        }
    }
}
