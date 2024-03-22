package com.example.ratatouille23client.amplify.auth;


import android.util.Log;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.auth.options.AuthFetchSessionOptions;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.result.step.AuthNextSignInStep;
import com.amplifyframework.core.Amplify;
import com.example.ratatouille23client.caching.RAT23Cache;
import com.example.ratatouille23client.model.Employee;
import com.example.ratatouille23client.model.Store;
import com.example.ratatouille23client.model.enumerations.Role;

import java.util.List;

public class AuthController {
    /**
     * Esegue il login dell'utente nella user pool Cognito tramite email e password.
     * Controlla lo stato dell'account che può essere:
     * 1) CONFIRM_SIGN_IN_PASSWORD se deve cambiare password durante il suo primo login.
     * 2) DONE se l'account è confermato e non ci sono richieste in attesa, quindi si può procedere oltre.
     * @param email
     * @param password
     * @param signInCallback interfaccia la cui implementazione viene definita direttamente quando serve
     *                       per descrivere il comportamento in caso di successo o fallimento della richiesta a Cognito.
     */
    public void signIn(String email, String password, SignInCallback signInCallback){
        Amplify.Auth.signIn(
                email,
                password,
                result -> {
                    AuthNextSignInStep nextStep = result.getNextStep();
                    switch(nextStep.getSignInStep()){
                        case CONFIRM_SIGN_IN_WITH_NEW_PASSWORD:
                            signInCallback.needNewPassword();
                            break;
                        case DONE:
                            getUserAttributes();
                            signInCallback.success();
                            break;
                    }
                },
                error -> {
                    signInCallback.error(error);
                }
        );
    }

    /**
     * Risponde al nextStep CONFIRM_SIGN_IN_WITH_NEW_PASSWORD con la nuova password.
     * @param newPassword
     */
    public void confirmSignInNewPassword(String newPassword, AuthCallback signInCallback){
        Amplify.Auth.confirmSignIn(
                newPassword,
                result -> {
                    if (result.isSignedIn()) {
                        getUserAttributes();
                        signInCallback.success();
                    } else {
                        Log.i("AuthQuickstart", "Confirm sign in not complete. There might be additional steps: " + result.getNextStep()); //TESTING
                    }
                },
                error -> signInCallback.error(error)
        );
    }

    /**
     * Ottiene i dati dell'utente attualmente in sessione dalla user pool Cognito.
     */
    public void getUserAttributes(){
        Amplify.Auth.fetchUserAttributes(
                result -> {
                    parseUserAttributes(result);
                },
                error -> {
                    error.printStackTrace(); //TESTING
                }
        );
    }

    /**
     * Usa gli attributi dell'utente in sessione ottenuti dalla user pool Cognito per creare un Employee attuale
     * e successivamente salvare le informazioni non sensibili nel db locale per richiamarle rapidamente quando
     * si farà riferimento all'utente attuale nell'applicazione.
     * @param result
     */
    private void parseUserAttributes(List<AuthUserAttribute> result) {
        Employee currentUser = new Employee();
       // String currentStore = null;
        for (AuthUserAttribute attribute: result) {
            if (attribute.getKey().getKeyString().equals("email"))
                currentUser.setEmail(attribute.getValue());
            else if (attribute.getKey().getKeyString().equals("given_name"))
                currentUser.setName(attribute.getValue());
            else if (attribute.getKey().getKeyString().equals("family_name"))
                currentUser.setSurname(attribute.getValue());
            else if (attribute.getKey().getKeyString().equals("custom:role"))
                currentUser.setRole(Role.valueOf(attribute.getValue()));


        }

        RAT23Cache<String, String> cache = RAT23Cache.getCacheInstance();
        cache.put("currentUserEmail", currentUser.getEmail());
        cache.put("currentUserName", currentUser.getName());
        cache.put("currentUserSurname", currentUser.getSurname());
        cache.put("currentUserRole", currentUser.getRole().name());
       // System.out.println(currentStore+ "Mocc a mammt");
        System.out.println(currentUser.getEmail());
        System.out.println(currentUser.getId());

    }

    /**
     * Esegue il sign out globale dell'utente, terminando la sua sessione.
     * Partial Sign Out può avvenire indica che il sign out è completato ma ha generato errori, come access token
     * non revocato.
     * Failed Sign Out indica che il sign out non ha avuto successo, pertanto le credenziali sono ancora nel device.
     */
    public void signOut(SignOutCallback signOutCallback){
            AuthSignOutOptions options = AuthSignOutOptions.builder()
                    .globalSignOut(true)
                    .build();

            Amplify.Auth.signOut(options, signOutResult -> {
                if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
                    System.out.println("LOGOUT DONE"); //TESTING
                    signOutCallback.success();
                } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut) {
                    signOutCallback.partialSignOut();
                } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut) {
                    signOutCallback.failedSignOut();
                }
            });
    }

    /**
     * Richiede a Cognito di resettare la password. L'utente riceve un'e-mail con un codice di verifica da
     * utilizzare in seguito per confermare la nuova password.
     * @param email
     * @param resetPasswordCallback
     */
    public void resetPassword(String email, AuthCallback resetPasswordCallback){
        Amplify.Auth.resetPassword(
                email,
                result -> {
                    Log.i("AuthQuickstart", result.toString());//TESTING
                    resetPasswordCallback.success();
                },
                error -> {
                    Log.e("AuthQuickstart", error.toString()); //TESTING
                    resetPasswordCallback.error(error);
                }
        );
    }

    /**
     * Conferma la nuova password per l'account (dopo richiesta di reset), sfruttando il codice di verifica
     * ricevuto per email.
     * @param email
     * @param newPassword
     * @param verificationCode
     * @param confirmResetPasswordCallback
     */
    public void confirmResetPassword(String email, String newPassword, String verificationCode, AuthCallback confirmResetPasswordCallback){
        Amplify.Auth.confirmResetPassword(
                email,
                newPassword,
                verificationCode,
                () -> {
                    Log.i("AuthQuickstart", "New password confirmed"); //TESTING
                    confirmResetPasswordCallback.success();
                },
                error -> {
                    Log.e("AuthQuickstart", error.toString());
                    confirmResetPasswordCallback.error(error);
                }
        );
    }

    public void checkUserStatus(AuthCallback callback){
        AuthFetchSessionOptions options = AuthFetchSessionOptions.builder()
                .forceRefresh(true)
                .build();

        Amplify.Auth.fetchAuthSession(options,
                result -> {
                    if (result.isSignedIn()){
                        Amplify.Auth.fetchUserAttributes(results -> {
                            parseUserAttributes(results);
                            callback.success();
                        }
                    ,error-> {
                            Log.e("AmplifyQuickstart", error.toString());
                    });
                }
            },
            error -> {
                callback.error(error);
            }
        );
    }
}
