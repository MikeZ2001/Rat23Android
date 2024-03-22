package com.example.ratatouille23client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ratatouille23client.amplify.auth.AuthController;
import com.example.ratatouille23client.amplify.auth.SignOutCallback;
import com.example.ratatouille23client.caching.RAT23Cache;
import com.example.ratatouille23client.model.enumerations.Role;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratatouille23client.databinding.ActivityMainBinding;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private AuthController authController = new AuthController();

    private NavigationView navigationView;
    private NavController navController;

    private final Logger logger = LoggerUtility.getLogger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.info("Inizializzazione schermata principale.");
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar() == null) {
            setSupportActionBar(binding.appBarMain.toolbar);
        }

        drawer = binding.drawerLayout;

        navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_ordinazione, R.id.nav_comande)
                .setOpenableLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setupHeaderView();
        setUpLogOutButton();

        logger.info("Terminata inizializzazione schermata principale.");
    }

    private void setupHeaderView() {
        logger.info("Inizializzazione header view.");
        View headerView = binding.navView.getHeaderView(0);

       RAT23Cache cache = RAT23Cache.getCacheInstance();
        String name = "Mike";
        String surname = "ZZ";
        String role = Role.CHEF.toString();
        String email = "mammt@gmaillc.om";

        TextView navNameSurnameRole = headerView.findViewById(R.id.userNameSurnameRole);
        TextView navEmail = headerView.findViewById(R.id.userEmail);
        navNameSurnameRole.setText(name+ " "+surname+" ("+role+")");
        navEmail.setText(email);

        String userRole = Role.ADMINISTRATOR.toString();
        String chefRole = (Role.CHEF.toString());

        if (userRole.equals(chefRole)){
            navController.navigate(R.id.nav_comande);
            navigationView.getMenu().getItem(0).setVisible(false);
        }

        logger.info("Terminata inizializzazione header view.");
    }

    @Override
    public void onBackPressed() {
        logger.info("Click Back.");
//        if (drawer.isDrawerOpen(GravityCompat.START)){
//            logger.info("Chiuso drawer layout.");
//            drawer.closeDrawer(GravityCompat.START);
//        }else
//            super.onBackPressed();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.navigateUp(navController, mAppBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setUpLogOutButton(){
        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info("Click su Logout.");
                logger.info("Avvio procedura di logout.");
                authController.signOut(new SignOutCallback() {
                    @Override
                    public void success() {
                        logger.info("Terminata procedura di logout. Reindirizzamento alla schermata di login.");
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    }

                    @Override
                    public void partialSignOut() {
                        logger.warning("Logout parziale.");
                    }

                    @Override
                    public void failedSignOut() {
                        logger.severe("Logout fallito.");
                    }
                });
            }
        });
    }
}