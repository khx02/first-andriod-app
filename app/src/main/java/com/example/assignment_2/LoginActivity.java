package com.example.assignment_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class  LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent().getExtras() != null){
            String usernameString = getIntent().getExtras().getString("username");
            TextView etUsername = findViewById(R.id.etLoginUsername);
            etUsername.setText(usernameString);
        }
    }

    public void onSignUpClick(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onLogInClick(View view){
        TextView tvUsername = findViewById(R.id.etLoginUsername);
        TextView tvPassword = findViewById(R.id.etLoginPass);
        String usernameString = tvUsername.getText().toString();
        String passString = tvPassword.getText().toString();

        SharedPreferences sharedPref = getSharedPreferences("ACC_DETAILS",Context.MODE_PRIVATE);
        String valueSaved = sharedPref.getString(usernameString, "DEFAULT_VALUE");

        if (passString.equals(valueSaved)){
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
        else {
            String msg = "Authentication failure: Username or Password incorrect";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}