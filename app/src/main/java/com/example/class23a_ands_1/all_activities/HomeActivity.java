package com.example.class23a_ands_1.all_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.class23a_ands_1.R;

public class HomeActivity extends AppCompatActivity {
    TextView txtWelcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtWelcomeMessage = findViewById(R.id.txtWelcomeMessage);

        Intent intent = getIntent();
        String userdatavalue = intent.getStringExtra("userdata");
        txtWelcomeMessage.setText("Welcome " + userdatavalue);

        Toast.makeText(this, userdatavalue, Toast.LENGTH_SHORT).show();
    }
}