package com.example.ratatouille23client;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ratatouille23client.amplify.auth.AuthCallback;
import com.example.ratatouille23client.amplify.auth.AuthController;
import com.example.ratatouille23client.amplify.auth.SignInCallback;
import com.example.ratatouille23client.api.EmployeeAPI;
import com.example.ratatouille23client.caching.RAT23Cache;
import com.example.ratatouille23client.databinding.ActivityLoginBinding;
import com.example.ratatouille23client.model.Employee;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding activityLoginBinding;

    private AuthController authController;

    private String email;
    private String newPassword;
    private String verificationCode;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoggerUtility.initializeLogger(getApplicationContext());

        logger.info("Inizializzazione schermata di login.");
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(activityLoginBinding.getRoot());

        authController = new AuthController();



        setupComponents();



        checkUserStatus();

        logger.info("Terminata inizializzazione schermata di login.");
    }

    private void checkUserStatus() {
        logger.info("Avvio procedura controllo stato di autenticazione.");
        authController.checkUserStatus(new AuthCallback() {
            @Override
            public void success() {
                authController.getUserAttributes();
                RAT23Cache cache = RAT23Cache.getCacheInstance();
                String userEmail = cache.get("currentUserEmail").toString();

                EmployeeAPI api = RetrofitInstance.getRetrofitInstance().create(EmployeeAPI.class);
                api.getEmployeeByEmail(userEmail).enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                        RAT23Cache cache = RAT23Cache.getCacheInstance();
                        cache.put("currentStore", response.body().getStore().getId());
                        cache.put("currentUserId", response.body().getId());

                        logger.info("Reindirizzamento alla schermata principale.");
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Employee> call, Throwable t) {
                        logger.severe(t.getMessage());
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login error: "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                    }
                });

                logger.info("Terminata procedura controllo stato di autenticazione. Reindirizzamento alla schermata principale.");

            }

            @Override
            public void error(Throwable error) {
                logger.severe(error.getMessage());
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Errore login: "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void setupComponents(){
        setupLoginButton();
        setupRetrievePasswordTextView();
    }

    private void setupRetrievePasswordTextView() {
        logger.info("Configurazione tasto recupera password.");
        activityLoginBinding.retrievePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Recupera Passsword.");
                logger.info("Reindirizzamento al dialog per il recupero della password.");
                showResetPasswordDialog();
            }
        });
        logger.info("Terminata configurazione tasto recupera password.");
    }

    private void setupLoginButton() {
        logger.info("Configurazione pulsante di login.");
        activityLoginBinding.loginButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Login.");
                String email = String.valueOf(activityLoginBinding.editTextEmailAddress.getText());
                String password = String.valueOf(activityLoginBinding.editTextPassword.getText());

                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                logger.info("Avvio procedura di autenticazione.");

                authController.signIn(email, password, new SignInCallback() {
                    @Override
                    public void success() {
                        logger.info("Terminata procedura di autenticazione.");
                        EmployeeAPI api = RetrofitInstance.getRetrofitInstance().create(EmployeeAPI.class);
                        api.getEmployeeByEmail(email).enqueue(new Callback<Employee>() {
                            @Override
                            public void onResponse(Call<Employee> call, Response<Employee> response) {
                                RAT23Cache cache = RAT23Cache.getCacheInstance();
                                cache.put("currentStore", response.body().getStore().getId());
                                cache.put("currentUserId", response.body().getId());
                                cache.put("currentName", response.body().getName());
                                cache.put("currentSurname",response.body().getSurname());

                                logger.info("Reindirizzamento alla schermata principale.");
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Employee> call, Throwable t) {
                                logger.severe(t.getMessage());
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login error: "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                            }
                        });
                    }

                    @Override
                    public void needNewPassword() {
                        logger.info("L'utente deve resettare la password prima. Reindirizzamento al dialog per il reset password.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showFirstLoginChangePasswordDialog();
                            }
                        });
                    }

                    @Override
                    public void error(Throwable error) {
                        logger.severe(error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Errore login: "+ error.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                            }
                        });
                    }
                });

            }
        });
        logger.info("Terminata configurazione pulsante di login.");
    }

    private void showResetPasswordDialog(){
        logger.info("Inizializzazione dialog per reset password.");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");
        builder.setCancelable(false);

        builder.setMessage("Inserisci l'e-mail dell'account di cui hai perso la password. Invieremo per e-mail un codice di verifica.");

        final EditText emailEditText = new EditText(this);
        emailEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEditText.setHint("E-mail");
        builder.setView(emailEditText);

        builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logger.info("Click su Conferma.");
                email = emailEditText.getText().toString();

                logger.info("Avvio procedura per reset password.");
                authController.resetPassword(email, new AuthCallback() {
                    @Override
                    public void success() {
                        logger.info("Terminata procedura per reset password. Reindirizzamento al dialog per il codice di verifica.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showVerificationCodeDialog();
                            }
                        });
                    }

                    @Override
                    public void error(Throwable error) {
                        logger.severe(error.getMessage());
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Errore login: "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });

        builder.show();

        logger.info("Terminata inizializzazione dialog per reset password.");
    }

    private void showVerificationCodeDialog(){
        logger.info("Inizializzazione dialog codice di verifica.");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Codice di Verifica");
        builder.setCancelable(false);

        builder.setMessage("Inserisci il codice di verifica che ti abbiamo inviato per e-mail.");

        final EditText verificationCodeEditText = new EditText(this);
        verificationCodeEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        verificationCodeEditText.setHint("Codice di verifica");
        builder.setView(verificationCodeEditText);

        builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logger.info("Click su Conferma.");
                verificationCode = String.valueOf(verificationCodeEditText.getText());
                logger.info("Reindirizzamento al dialog per nuova password.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showNewPasswordResetDialog();
                    }
                });
            }
        });

        builder.show();
        logger.info("Terminata inizializzazione dialog codice di verifica.");
    }

    private void showNewPasswordResetDialog() {
        logger.info("Inizializzazione dialog per nuova password.");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuova Password Richiesta");
        builder.setCancelable(false);

        builder.setMessage("Inserisci la tua nuova password.");

        final EditText newPasswordEditText = new EditText(this);
        newPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPasswordEditText.setHint("Nuova password");
        builder.setView(newPasswordEditText);

        builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logger.info("Click su Conferma.");
                newPassword = newPasswordEditText.getText().toString();

                logger.info("Avvio procedura conferma nuova password.");
                authController.confirmResetPassword(email, newPassword, verificationCode, new AuthCallback() {
                    @Override
                    public void success() {
                        logger.info("Terminata procedura conferma nuova password.");
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Successo! Ora puoi utilizzarla.", Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void error(Throwable error) {
                        logger.severe(error.getMessage());
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Errore login: "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show());

                    }
                });

            }
        });

        builder.show();
        logger.info("Terminata inizializzazione dialog per nuova password.");
    }

    private void showFirstLoginChangePasswordDialog(){
        logger.info("Inizializzazione dialog per forzare reser password.");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuova Password Richiesta");
        builder.setCancelable(false);

        builder.setMessage("Prima di poter continuare è necessario che tu inserisca la tua nuova password.");

        final EditText newPasswordEditText = new EditText(this);
        newPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPasswordEditText.setHint("Nuova password");
        builder.setView(newPasswordEditText);

        builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logger.info("Click su Conferma.");
                newPassword = newPasswordEditText.getText().toString();
                logger.info("Avvio procedura per confermare la nuova password.");
                authController.confirmSignInNewPassword(newPassword, new AuthCallback() {
                    @Override
                    public void success() {
                        logger.info("Terminata procedura per confermare la nuova password.");
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Successo! Ora puoi utilizzarla.", Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void error(Throwable error) {
                        logger.severe(error.getMessage());
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login error: "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });

        builder.show();
        logger.info("Terminata inizializzazione dialog per forzare reser password.");
    }
}