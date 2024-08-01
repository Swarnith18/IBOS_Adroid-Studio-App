package com.example.ibos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class First extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public void login(View view) {
        Intent i = new Intent(First.this,UserLogin.class);
        startActivity(i);
    }

    public void signup(View view) {
        Intent i = new Intent(First.this,Signup.class);
        startActivity(i);
    }
}