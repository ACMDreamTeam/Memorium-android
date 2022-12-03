package com.acmdreamteam.memorium;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        user = (User) getIntent().getExtras().getSerializable("object");

        String name = user.getName();
        String age = user.getAge();
        String marriage = user.getMarried();






    }
}