package com.example.jinder_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class JobDetailActivity extends AppCompatActivity {

    public static final String EXTERNAL_URL = "externalUrl";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        Intent intent = getIntent();
        Job job = (Job) intent.getSerializableExtra("Job");

        TextView jobTitle = (TextView) findViewById(R.id.jobTitle);
        jobTitle.setText(job.getJobTitle());

        TextView companyName = (TextView) findViewById(R.id.companyName);
        companyName.setText(job.getCompanyName());

        TextView jobDescription = (TextView) findViewById(R.id.jobDescription);
        jobDescription.setMovementMethod(new ScrollingMovementMethod());
        jobDescription.setText(job.getJobDescription());

        TextView skills = (TextView) findViewById(R.id.skills);
        skills.setMovementMethod(new ScrollingMovementMethod());
        skills.setText("Skills:\n" + (job.getSkills().equals("null") ? "" : job.getSkills()));

        Button applyBtn = (Button) findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchExternalPage(job.getJobAppUrl());
            }
        });
    }

    void launchExternalPage(String externalUrl) {
        Intent intent = new Intent(JobDetailActivity.this, WebViewActivity.class);
        intent.putExtra(EXTERNAL_URL, externalUrl);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myAccount:
                Intent data = new Intent(JobDetailActivity.this, ViewAccountActivity.class);
                startActivity(data);
                break;
            case R.id.uploadResume:
                Intent upload = new Intent(JobDetailActivity.this, UploadResumeActivity.class);
                startActivity(upload);
                break;
            case R.id.logout:
                SharedPreferences pref =
                        getSharedPreferences("user_credentials", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(JobDetailActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }
}