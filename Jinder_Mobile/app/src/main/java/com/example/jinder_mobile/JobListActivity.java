package com.example.jinder_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JobListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        CustomAdapter adapter = new CustomAdapter(this, 0);

        EditText query = (EditText) findViewById(R.id.query);
        Button searchBtn = (Button) findViewById(R.id.searchBtn);
        TextView loadMsg = (TextView) findViewById(R.id.loadMsg);
        loadMsg.setVisibility(View.VISIBLE);

        SharedPreferences pref =
                getSharedPreferences("user_credentials", MODE_PRIVATE);
        Boolean resumeUploaded = pref.getBoolean("resumeUploaded", false);
        String userId = pref.getString("userId", null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url;
                    if (resumeUploaded) {
                        url = new URL("http://10.0.2.2:8080/api/jobs/sorted/" + userId);
                    }
                    else {
                        url = new URL("http://10.0.2.2:8080/api/jobs/");
                    }

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
                    JSONArray jArray = new JSONArray(result);

                    Job[] jobs = new Job[jArray.length()];
                    for (int i=0; i < jArray.length(); i++)
                    {
                        try {
                            JSONObject jObject = jArray.getJSONObject(i);
                            // Pulling items from the array
                            jobs[i] = new Job(jObject.getString("jobTitle"),
                                    jObject.getString("companyName"),
                                    jObject.getString("jobDescription"),
                                    jObject.getString("skills"),
                                    jObject.getString("jobAppUrl"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setData(jobs);
                            ListView listView = findViewById(R.id.listView);
                            if (listView != null) {
                                loadMsg.setVisibility(View.GONE);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {
                                        Intent intent = new Intent(JobListActivity.this,
                                                JobDetailActivity.class);
                                        intent.putExtra("Job", jobs[position]);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                loadMsg.setVisibility(View.VISIBLE);
                String keyword = query.getText().toString().trim().replace(" ", "+");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url;
                            if (resumeUploaded) {
                                url = new URL("http://10.0.2.2:8080/api/jobs/sorted/" +
                                        userId + "/" + keyword);
                            }
                            else {
                                url = new URL("http://10.0.2.2:8080/api/jobs/" + keyword);
                            }

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
                            JSONArray jArray = new JSONArray(result);

                            Job[] jobs = new Job[jArray.length()];
                            for (int i=0; i < jArray.length(); i++)
                            {
                                try {
                                    JSONObject jObject = jArray.getJSONObject(i);
                                    // Pulling items from the array
                                    jobs[i] = new Job(jObject.getString("jobTitle"),
                                            jObject.getString("companyName"),
                                            jObject.getString("jobDescription"),
                                            jObject.getString("skills"),
                                            jObject.getString("jobAppUrl"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setData(jobs);
                                    ListView listView = findViewById(R.id.listView);
                                    if (listView != null) {
                                        loadMsg.setVisibility(View.GONE);
                                        listView.setAdapter(adapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view,
                                                                    int position, long id) {
                                                Intent intent = new Intent(JobListActivity.this,
                                                        JobDetailActivity.class);
                                                intent.putExtra("Job", jobs[position]);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
            case R.id.myAccount:
                Intent data = new Intent(JobListActivity.this, ViewAccountActivity.class);
                startActivity(data);
                break;
            case R.id.uploadResume:
                Intent upload = new Intent(JobListActivity.this, UploadResumeActivity.class);
                startActivity(upload);
                break;
            case R.id.logout:
                SharedPreferences pref =
                        getSharedPreferences("user_credentials", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(JobListActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }
}