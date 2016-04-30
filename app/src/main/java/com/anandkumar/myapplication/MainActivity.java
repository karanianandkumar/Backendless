package com.anandkumar.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.backendless.Backendless;

public class MainActivity extends AppCompatActivity {

    public static final String APP_ID="B95DAA23-ED7C-373A-FF03-DDB3ECB1DA00";
    public static final String SECRET_KEY="47AD7C04-8F50-9875-FF4E-6B7EBFCA0600";
    public static final String VERSION="v1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MenuFragment menuFragment=new MenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,menuFragment).commit();

        Backendless.initApp(this,APP_ID,SECRET_KEY,VERSION);
    }
}
