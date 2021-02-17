package com.example.jinder_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_register;
    private EditText ui_username,  ui_fullname, ui_password, ui_password1, ui_emailAddress,
            ui_address, ui_phoneNo;
    private String username, password, password1, fullname, email, address, phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {

        btn_register = findViewById(R.id.btn_register);
        ui_username = findViewById(R.id.username);
        ui_fullname = findViewById(R.id.fullname);
        ui_password = findViewById(R.id.password);
        ui_password1 = findViewById(R.id.password1);
        ui_emailAddress = findViewById(R.id.emailAddress);
        ui_address = findViewById(R.id.address);
        ui_phoneNo = findViewById(R.id.phoneNo);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                getEditString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your username",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password1)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password again",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(fullname)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your fullname",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(address)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your address",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(phoneNo)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your phone number",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (!password.equals(password1)) {
                    Toast.makeText(RegisterActivity.this, "The password do not match",
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

                                if (!sb.toString().equals("")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this,
                                                    "The username already exists",
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    });
                                }
                                else {
                                    url = new URL("http://10.0.2.2:8080/api/user/save/" +
                                            username + '&' + password + '&' + fullname + '&' + email +
                                            '&' + address + '&' + phoneNo);
                                    conn = (HttpURLConnection) url.openConnection();
                                    conn.setRequestMethod("POST");
                                    conn.connect();
                                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    sb = new StringBuilder();
                                    String line1 = null;
                                    while ((line1 = rd.readLine()) != null)
                                    {
                                        sb.append(line1 + '\n');
                                    }
                                    rd.close();
                                    conn.disconnect();
                                    if (sb.toString().equals("")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(RegisterActivity.this,
                                                        "Registered successfully!",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent data = new Intent(RegisterActivity.this,
                                                        HomeActivity.class);
                                                data.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                startActivity(data);
                                            }
                                        });
                                    }
                                    else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(RegisterActivity.this,
                                                        "Registration unsuccessful." +
                                                                " Please check all fields.",
                                                        Toast.LENGTH_SHORT).show();
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

    private void getEditString() {
        username = ui_username.getText().toString().trim();
        password = ui_password.getText().toString().trim();
        password1 = ui_password1.getText().toString().trim();
        fullname = ui_fullname.getText().toString().trim();
        email = ui_emailAddress.getText().toString().trim();
        address = ui_address.getText().toString().trim();
        phoneNo = ui_phoneNo.getText().toString().trim();

    }
}