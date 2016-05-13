package com.anandkumar.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        MainMenuFragment loginFragment=new MainMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_menu_container,loginFragment).commit();
    }
}
