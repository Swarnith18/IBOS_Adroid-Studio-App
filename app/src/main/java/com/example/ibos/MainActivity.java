package com.example.ibos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {
Handler handler= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent i= new Intent (MainActivity.this,First.class);
               startActivity(i);
               finish();
           }
       },1000);
    }
}