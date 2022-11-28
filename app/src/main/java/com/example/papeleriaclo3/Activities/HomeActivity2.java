package com.example.papeleriaclo3.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.papeleriaclo3.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
    }


}