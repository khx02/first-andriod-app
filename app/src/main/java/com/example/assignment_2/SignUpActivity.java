package com.example.assignment_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
    }
    public void onSignUpButtonClick(View view){
        Intent intent = new Intent(this, LoginActivity.class);

        TextView tvUsername = findViewById(R.id.etUsername);
        TextView tvPassword = findViewById(R.id.etPass);
        TextView tvPassCon = findViewById(R.id.etPassCon);

        String usernameString = tvUsername.getText().toString();
        String passString = tvPassword.getText().toString();
        String passConString = tvPassCon.getText().toString();

        if (validator(passString, passConString, usernameString)){
            saveDataToSharedPreference(usernameString, passString);
            intent.putExtra("username", usernameString);
            startActivity(intent);
        }
    }
    public void onLogInButtonClick(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    private boolean validator(String tvPassword, String tvPassCon, String usernameString){
        // check pw with pw confirmation
        boolean flag = false;
        String message;
        if (tvPassword.equals(tvPassCon) && !tvPassword.isEmpty()) {
            message = "Sign Up Successful";
            flag = true;
        }
        else{
            message = "Password doesn't match, please try again";
        }
        if (usernameString.isEmpty()){
            message = "Username can't be empty.";
            flag = false;
        }
        String REGEX = "^[a-zA-Z0-9]*[a-zA-Z][a-zA-Z0-9]*$";
        if (!usernameString.matches(REGEX)){
            message = "Username must be alphanumeric";
            flag = false;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return flag;
    }
    private void saveDataToSharedPreference(String usernameValue, String passwordValue){
        // initialise shared preference class variable to access Android's persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("ACC_DETAILS", MODE_PRIVATE);

        // use .edit function to access file using Editor variable
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // save key-value pairs to the shared preference file
        editor.putString(usernameValue, passwordValue);

        // doing in background is very common practice for any File Input/Output operations
        editor.apply();
    }

}