package com.example.qr.qscan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.qr.qscan.constant.Constant;
import com.example.qr.qscan.network.HTTPManager;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.qr.qscan.constant.Constant.CREDENTIALS;
import static com.example.qr.qscan.constant.Constant.LOGIN_URL;
import static com.example.qr.qscan.constant.Constant.NO_AUTH;
import static com.example.qr.qscan.constant.Constant.POST;
import static com.example.qr.qscan.constant.Constant.SHARED_PREFERENCES;
import static com.example.qr.qscan.constant.Constant.TOKEN_PREFERENCE;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText username;
    TextInputEditText password;
    Button login;
    HTTPManager httpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        persistentLoginCheck();
        init();
    }

    private void persistentLoginCheck() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
                MODE_PRIVATE);

        String token = sharedPreferences.getString(TOKEN_PREFERENCE, "");

        if (!token.equals("")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(CREDENTIALS, token);
            startActivity(intent);
            finish();
        }
    }

    private void init() {
        login = findViewById(R.id.login_btn);
        username = findViewById(R.id.login_username_tie);
        password = findViewById(R.id.login_password_tie);

        login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                if (!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    httpManager = new HTTPManager() {
                        @Override
                        protected void onPostExecute(String s) {
                            try {

                                JSONObject obj = new JSONObject(s);

                                String token = obj.getString("token");

                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
                                        MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(TOKEN_PREFERENCE, token);
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra(CREDENTIALS, token);
                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Credentials not good or server down",
                                        Toast.LENGTH_SHORT).show();
                                Log.e("nasty_error", e.toString());
                            }
                        }
                    };

                    String JSONCredentials = "{\"username\":\"" + username.getText().toString() + "\", " +
                            "\"password\":\"" + password.getText().toString() + "\"}";

                    httpManager.execute(LOGIN_URL, POST, NO_AUTH, JSONCredentials);
                } else {
                    if (username.getText().toString().equals("")) {
                        username.requestFocus();
                    } else if (password.getText().toString().equals("")) {
                        password.requestFocus();
                    }
                }
            }
        });
    }
}