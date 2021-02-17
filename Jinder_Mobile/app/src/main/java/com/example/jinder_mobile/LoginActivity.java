package com.example.jinder_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private String username, password, dbPassword, resumeUrl, fullname, userId, emailAddress,
            address, phoneNo;
    private EditText user_input, password_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {

        btn_login=findViewById(R.id.btn_login);
        user_input=findViewById(R.id.user_input);
        password_input=findViewById(R.id.password_input);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                username = user_input.getText().toString().trim();
                password = password_input.getText().toString().trim();
                if(TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginActivity.this, "Please enter username",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter your password.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL("http://10.0.2.2:8080/api/user/" + username);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                conn.connect();
                                BufferedReader rd  = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                StringBuilder sb = new StringBuilder();
                                String line = null;
                                while ((line = rd.readLine()) != null)
                                {
                                    sb.append(line + '\n');
                                }
                                rd.close();
                                conn.disconnect();
                                String result = sb.toString();

                                if (result.equals("")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this,
                                                    "The username does not exist",
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    });
                                }
                                else {
                                    JSONObject jObject = new JSONObject(result);
                                    dbPassword = jObject.getString("password");
                                    if(password.equals(dbPassword)) {
                                        resumeUrl = jObject.getString("resumeUrl");
                                        fullname = jObject.getString("fullName");
                                        userId = jObject.getString("id");
                                        emailAddress = jObject.getString("emailAddress");
                                        address = jObject.getString("address");
                                        phoneNo = jObject.getString("phoneNo");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                SharedPreferences pref =
                                                        getSharedPreferences("user_credentials",
                                                                MODE_PRIVATE);
                                                SharedPreferences.Editor editor = pref.edit();
                                                editor.putString("username", username);
                                                editor.putString("fullname", fullname);
                                                if (resumeUrl.equals("null"))
                                                    editor.putBoolean("resumeUploaded", false);
                                                else
                                                    editor.putBoolean("resumeUploaded", true);
                                                editor.putString("userId", userId);
                                                editor.putString("email", emailAddress);
                                                editor.putString("address", address);
                                                editor.putString("phoneNo", phoneNo);
                                                editor.putString("password", password);
                                                editor.commit();

                                                Toast.makeText(LoginActivity.this,
                                                        "Login successful!",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent data = new Intent(LoginActivity.this,
                                                        JobListActivity.class);
                                                startActivity(data);
                                            }
                                        });
                                    }
                                    else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity.this,
                                                        "Wrong password entered",
                                                        Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        });
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }
}