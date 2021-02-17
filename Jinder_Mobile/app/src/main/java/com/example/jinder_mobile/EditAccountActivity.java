package com.example.jinder_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditAccountActivity extends AppCompatActivity {

    private EditText fullName, userName, password, email, address, phone;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        SharedPreferences pref =
                getSharedPreferences("user_credentials", MODE_PRIVATE);
        String idPref = pref.getString("userId", null);

        fullName = findViewById(R.id.editFullName);
        userName = findViewById(R.id.editUserName);
        password = findViewById(R.id.editPassword2);
        email = findViewById(R.id.editEmail);
        address = findViewById(R.id.editAddress);
        phone = findViewById(R.id.editPhone);

        fullName.setText(pref.getString("fullname", null));
        userName.setText(pref.getString("username", null));
        password.setText(pref.getString("password", null));
        email.setText(pref.getString("email", null));
        address.setText(pref.getString("address", null));
        phone.setText(pref.getString("phoneNo", null));

        saveBtn = (Button) findViewById(R.id.saveBtn);

        if (saveBtn != null) {
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    String eUserName = userName.getText().toString().trim();
                    String ePassword = password.getText().toString().trim();
                    String eFullName = fullName.getText().toString().trim();
                    String eEmailAddress = email.getText().toString().trim();
                    String eAddress = address.getText().toString().trim();
                    String ePhoneNo = phone.getText().toString().trim();

                    if (TextUtils.isEmpty(eUserName)) {
                        Toast.makeText(EditAccountActivity.this, "Please enter your username",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(ePassword)) {
                        Toast.makeText(EditAccountActivity.this, "Please enter your password",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(eFullName)) {
                        Toast.makeText(EditAccountActivity.this, "Please enter your fullname",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(eEmailAddress)) {
                        Toast.makeText(EditAccountActivity.this, "Please enter your email",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(eAddress)) {
                        Toast.makeText(EditAccountActivity.this, "Please enter your address",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(ePhoneNo)) {
                        Toast.makeText(EditAccountActivity.this, "Please enter your phone number",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL("http://10.0.2.2:8080/api/user/" + eUserName);
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setRequestMethod("GET");
                                    conn.connect();
                                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    StringBuilder sb = new StringBuilder();
                                    String line = null;
                                    while ((line = rd.readLine()) != null) {
                                        sb.append(line + '\n');
                                    }
                                    rd.close();
                                    conn.disconnect();

                                    if (!sb.toString().equals("") && !eUserName.equals(
                                            pref.getString("username", null))) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(EditAccountActivity.this,
                                                        "The username already exists",
                                                        Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        });
                                    } else {
                                        url = new URL("http://10.0.2.2:8080/api/user/edit/" +
                                                eUserName + "&" +
                                                ePassword + "&" +
                                                eFullName + "&" +
                                                eEmailAddress + "&" +
                                                eAddress + "&" +
                                                ePhoneNo + "&" +
                                                idPref
                                        );

                                        conn = (HttpURLConnection) url.openConnection();
                                        conn.setRequestMethod("POST");
                                        conn.connect();
                                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                        sb = new StringBuilder();
                                        String line1 = null;
                                        while ((line1 = rd.readLine()) != null) {
                                            sb.append(line1 + '\n');
                                        }
                                        rd.close();
                                        conn.disconnect();
                                        if (sb.toString().equals("")) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SharedPreferences pref =
                                                            getSharedPreferences("user_credentials",
                                                                    MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("username", eUserName);
                                                    editor.putString("fullname", eFullName);
                                                    editor.putString("email", eEmailAddress);
                                                    editor.putString("address", eAddress);
                                                    editor.putString("phoneNo", ePhoneNo);
                                                    editor.commit();

                                                    Toast.makeText(EditAccountActivity.this,
                                                            "Saved successfully!",
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent data = new Intent(EditAccountActivity.this,
                                                            ViewAccountActivity.class);
//                                                    data.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                                            Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    startActivity(data);
                                                    finish();
                                                }
                                            });
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(EditAccountActivity.this,
                                                            "Cannot save changes." +
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.myAccount:
                Intent data = new Intent(EditAccountActivity.this, ViewAccountActivity.class);
                startActivity(data);
                break;
            case R.id.uploadResume:
                Intent upload = new Intent(EditAccountActivity.this, UploadResumeActivity.class);
                startActivity(upload);
                break;
            case R.id.logout:
                SharedPreferences pref =
                        getSharedPreferences("user_credentials", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(EditAccountActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }
}