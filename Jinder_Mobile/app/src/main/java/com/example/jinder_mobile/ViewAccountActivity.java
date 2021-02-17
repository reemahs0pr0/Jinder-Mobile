package com.example.jinder_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewAccountActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        SharedPreferences pref =
                getSharedPreferences("user_credentials", MODE_PRIVATE);
        Boolean resumeUploaded = pref.getBoolean("resumeUploaded", false);
        String fullname = pref.getString("fullname", null);
        String username = pref.getString("username", null);
        String email = pref.getString("email", null);
        String address = pref.getString("address", null);
        String phoneNo = pref.getString("phoneNo", null);

        TextView ui_fullname = (TextView) findViewById(R.id.fullname);
        ui_fullname.setText("Name: " + fullname);
        TextView ui_username = (TextView) findViewById(R.id.username);
        ui_username.setText("Username: " + username);
        TextView ui_emailAddress = (TextView) findViewById(R.id.emailAddress);
        ui_emailAddress.setText("Email Address: " + email);
        TextView ui_address = (TextView) findViewById(R.id.address);
        ui_address.setText("Home Address: " + address);
        TextView ui_phoneNo = (TextView) findViewById(R.id.phoneNo);
        ui_phoneNo.setText("Phone Number: " + phoneNo);
        TextView ui_resumeUploaded = (TextView) findViewById(R.id.resumeUploaded);
        if (resumeUploaded) {
            ui_resumeUploaded.setText("Resume Uploaded: YES");
        }
        else {
            ui_resumeUploaded.setText("Resume Uploaded: NO");
        }

        Button editBtn = (Button) findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAccountActivity.this,
                        EditAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
            case R.id.uploadResume:
                Intent upload = new Intent(ViewAccountActivity.this, UploadResumeActivity.class);
                startActivity(upload);
                break;
            case R.id.logout:
                SharedPreferences pref =
                        getSharedPreferences("user_credentials", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(ViewAccountActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }
}