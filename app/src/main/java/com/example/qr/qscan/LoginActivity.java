package com.example.qr.qscan;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText username;
    TextInputEditText password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        login = findViewById(R.id.login_btn);
        username = findViewById(R.id.login_username_tie);
        password = findViewById(R.id.login_password_tie);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!username.getText().toString().equals("") && !password.getText().toString().equals("")){
                    //here will actually be the rest call
                    //a loading icon can be displayed
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //a much complete verification with focus on empty fields
                    Toast.makeText(getApplicationContext(), "insert credentials pls", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
