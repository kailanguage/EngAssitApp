package com.kailang.engassit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kailang.engassit.data.Sync;
import com.kailang.engassit.data.entity.User;
import com.kailang.engassit.ui.login.LoginActivity;
import com.kailang.engassit.ui.login.LoginViewModel;
import com.kailang.engassit.ui.person.PersonViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import static com.kailang.engassit.data.Sync.currentUser;

public class MainActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private Sync sync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_word, R.id.navigation_sentence,R.id.navigation_test, R.id.navigation_person)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if(currentUser==null) {
            sync = new Sync(this);
            //自动登录
            loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
            SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
            if (!sp.getAll().isEmpty()) {
                String password = sp.getString("upassword", "fail");
                Integer userno = sp.getInt("userno", -1);
                if (!password.equals("fail") && !userno.equals(-1)) {
                    sync.login(userno, password, loginViewModel, false);
                    Log.e("自动登录", userno + " " + password);
                }
            }else{
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigateUp();
        return super.onSupportNavigateUp();
    }

}
